package ru.practice5.main.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import ru.practice5.main.dto.InstanceArrangementDto;
import ru.practice5.main.dto.NewCorporateSettlementAccountDto;
import ru.practice5.main.dto.NewCorporateSettlementInstanceDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/** Real HTTP dispatch, transactions and PostgreSQL unique-key conflicts, without mocks. */
@SpringBootTest
@AutoConfigureMockMvc
@EnabledIfSystemProperty(named = "settlement.postgresConcurrency", matches = "true")
class ConcurrentCreationTests {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired PlatformTransactionManager transactions;

    @BeforeEach
    void prepare() {
        clean();
        jdbc.update("INSERT INTO tpp_product(id,number) VALUES(9001,'REVIEW-CONCURRENT-EXISTING')");
    }

    @AfterEach
    void clean() {
        jdbc.update("DELETE FROM agreement WHERE product_id IN (SELECT id FROM tpp_product WHERE number LIKE 'REVIEW-CONCURRENT-%')");
        jdbc.update("DELETE FROM tpp_product_register WHERE product_id IN (SELECT id FROM tpp_product WHERE number LIKE 'REVIEW-CONCURRENT-%')");
        jdbc.update("DELETE FROM tpp_product WHERE number LIKE 'REVIEW-CONCURRENT-%'");
        jdbc.update("UPDATE account SET bussy=false WHERE id BETWEEN 1 AND 3");
    }

    NewCorporateSettlementInstanceDto product() {
        var dto = new NewCorporateSettlementInstanceDto();
        dto.setProductType("NSO"); dto.setProductCode("03.012.002");
        dto.setRegisterType("03.012.002_47533_ComSoLd"); dto.setMdmCode("15");
        dto.setContractNumber("REVIEW-CONCURRENT-CONTRACT");
        dto.setContractDate(LocalDateTime.of(2026,11,14,12,0)); dto.setPriority(0);
        dto.setContractId(118); dto.setBranchCode("0022"); dto.setIsoCurrencyCode("800");
        dto.setUrgencyCode("00"); return dto;
    }

    void race(String endpoint, Object dto, String blocker, boolean rollbackBlocker) throws Exception {
        String body = json.writeValueAsString(dto);
        var executor = Executors.newFixedThreadPool(3);
        var start = new CyclicBarrier(3);
        var futures = new ArrayList<Future<MvcResult>>();
        try {
            new TransactionTemplate(transactions).execute(tx -> {
                jdbc.execute(blocker);
                for (int i=0;i<3;i++) futures.add(executor.submit(() -> {
                    start.await(10, TimeUnit.SECONDS);
                    return mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();
                }));
                long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(15);
                int blocked = 0;
                while (System.nanoTime() < deadline) {
                    // Activity snapshots are cached within the coordinating transaction.
                    jdbc.execute("SELECT pg_stat_clear_snapshot()");
                    blocked = jdbc.queryForObject("SELECT COUNT(*) FROM pg_stat_activity WHERE datname=current_database() AND pid<>pg_backend_pid() AND wait_event_type='Lock'", Integer.class);
                    if (blocked == 3) break;
                    try { Thread.sleep(30); } catch (InterruptedException e) { throw new RuntimeException(e); }
                }
                assertEquals(3, blocked, "All three real requests must reach the conflicting write/lock");
                if (rollbackBlocker) tx.setRollbackOnly();
                return null;
            });
            int ok=0, rejected=0;
            for (var future:futures) {
                var response = future.get(20, TimeUnit.SECONDS).getResponse();
                if (response.getStatus()==200) ok++;
                else {
                    assertEquals(400,response.getStatus(),response.getContentAsString());
                    var message=json.readTree(response.getContentAsByteArray()).path("error").asText();
                    assertFalse(message.isEmpty()); assertTrue(message.contains("уже существует"),message);
                    assertFalse(message.contains("insert into")); rejected++;
                }
            }
            assertEquals(1,ok); assertEquals(2,rejected);
        } finally {
            executor.shutdownNow(); assertTrue(executor.awaitTermination(5,TimeUnit.SECONDS));
        }
    }

    @Test
    void identicalRegisterRequestsCreateOneRegisterAndReserveOneAccount() throws Exception {
        var dto = new NewCorporateSettlementAccountDto();
        dto.setInstanceId(9001L); dto.setRegistryTypeCode("03.012.002_47533_ComSoLd");
        dto.setBranchCode("0022"); dto.setCurrencyCode("800"); dto.setMdmCode("15"); dto.setPriorityCode("00");
        race("/corporate-settlement-account/create",dto,"SELECT id FROM account WHERE id=1 FOR UPDATE",false);
        assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM tpp_product_register WHERE product_id=9001",Integer.class));
        assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM account WHERE bussy=true",Integer.class));
    }

    @Test
    void identicalContractRequestsCreateOneProductAndReserveOneAccount() throws Exception {
        race("/corporate-settlement-instance/create",product(),
                "INSERT INTO tpp_product(id,number) VALUES(9900,'REVIEW-CONCURRENT-CONTRACT')",true);
        assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM tpp_product WHERE number='REVIEW-CONCURRENT-CONTRACT'",Integer.class));
        assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM tpp_product_register WHERE product_id IN (SELECT id FROM tpp_product WHERE number='REVIEW-CONCURRENT-CONTRACT')",Integer.class));
        assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM account WHERE bussy=true",Integer.class));
    }

    @Test
    void identicalAgreementRequestsCreateOneAgreementWithoutReservingAccounts() throws Exception {
        var dto=product();dto.setInstanceId(9001);
        var agreement=new InstanceArrangementDto();agreement.setNumber("REVIEW-CONCURRENT-AGREEMENT");
        dto.setInstanceArrangementDto(List.of(agreement));
        race("/corporate-settlement-instance/create",dto,
                "INSERT INTO agreement(id,product_id,number) VALUES(9900,9001,'REVIEW-CONCURRENT-AGREEMENT')",true);
        assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM agreement WHERE number='REVIEW-CONCURRENT-AGREEMENT'",Integer.class));
        assertEquals(0,jdbc.queryForObject("SELECT COUNT(*) FROM account WHERE bussy=true",Integer.class));
    }

    @Test
    void repeatedAgreementInOneRequestRollsBackTheEarlierInsert() throws Exception {
        var dto=product();dto.setInstanceId(9001);
        var agreement=new InstanceArrangementDto();agreement.setNumber("REVIEW-CONCURRENT-AGREEMENT");
        dto.setInstanceArrangementDto(List.of(agreement,agreement));
        var response=mvc.perform(post("/corporate-settlement-instance/create")
                .contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(dto))).andReturn().getResponse();
        assertEquals(400,response.getStatus());
        assertEquals(0,jdbc.queryForObject("SELECT COUNT(*) FROM agreement WHERE product_id=9001",Integer.class));
    }
}
