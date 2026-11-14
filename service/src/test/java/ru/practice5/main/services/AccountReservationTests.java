package ru.practice5.main.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import ru.practice5.main.dto.NewCorporateSettlementAccountDto;
import ru.practice5.main.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Runs on H2 by default; datasource properties also allow a real PostgreSQL run. */
@SpringBootTest
class AccountReservationTests {
    @Autowired
    private CsaService service;
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private PlatformTransactionManager transactions;

    @BeforeEach
    void prepare() {
        clean();
        for (long id = 9001; id <= 9004; id++) {
            jdbc.update("INSERT INTO tpp_product(id, number) VALUES (?, ?)", id, "reservation-" + id);
        }
    }

    @AfterEach
    void clean() {
        jdbc.update("DELETE FROM tpp_product_register");
        jdbc.update("UPDATE account SET bussy = false");
        jdbc.update("DELETE FROM tpp_product WHERE id BETWEEN 9001 AND 9004");
    }

    private NewCorporateSettlementAccountDto request(long product) {
        var dto = new NewCorporateSettlementAccountDto();
        dto.setInstanceId(product);
        dto.setRegistryTypeCode("03.012.002_47533_ComSoLd");
        dto.setBranchCode("0022");
        dto.setCurrencyCode("800");
        dto.setMdmCode("15");
        dto.setPriorityCode("00");
        return dto;
    }

    @Test
    void assignsDistinctAccountsAndRejectsExhaustionWithoutExtraRegister() {
        for (long id = 9001; id <= 9003; id++) {
            service.create(request(id));
        }
        assertEquals(3, jdbc.queryForObject(
                "SELECT COUNT(DISTINCT account) FROM tpp_product_register", Integer.class));
        assertEquals(3, jdbc.queryForObject(
                "SELECT COUNT(*) FROM account WHERE bussy = true", Integer.class));
        assertThrows(NotFoundException.class, () -> service.create(request(9004)));
        assertEquals(3, jdbc.queryForObject("SELECT COUNT(*) FROM tpp_product_register", Integer.class));
    }

    @Test
    void parallelTransactionsNeverAssignTheSameAccount() throws Exception {
        var executor = Executors.newFixedThreadPool(3);
        var start = new CyclicBarrier(3);
        try {
            var calls = new ArrayList<Callable<String>>();
            for (long id = 9001; id <= 9003; id++) {
                final long product = id;
                calls.add(() -> {
                    start.await(10, TimeUnit.SECONDS);
                    return service.create(request(product)).getData().getAccountId();
                });
            }
            var ids = new HashSet<String>();
            for (var future : executor.invokeAll(calls, 20, TimeUnit.SECONDS)) {
                ids.add(future.get(1, TimeUnit.SECONDS));
            }
            assertEquals(3, ids.size());
            assertEquals(3, jdbc.queryForObject(
                    "SELECT COUNT(DISTINCT account) FROM tpp_product_register", Integer.class));
            assertEquals(3, jdbc.queryForObject(
                    "SELECT COUNT(*) FROM account WHERE bussy = true", Integer.class));
        } finally {
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }
    }

    @Test
    void rollbackReleasesReservationAndRemovesRegister() {
        var tx = new TransactionTemplate(transactions);
        assertThrows(IllegalStateException.class, () -> tx.execute(status -> {
            service.create(request(9001));
            throw new IllegalStateException("Abort the enclosing product transaction");
        }));
        assertEquals(0, jdbc.queryForObject("SELECT COUNT(*) FROM tpp_product_register", Integer.class));
        assertEquals(0, jdbc.queryForObject("SELECT COUNT(*) FROM account WHERE bussy = true", Integer.class));
        service.create(request(9001));
        assertEquals(1, jdbc.queryForObject("SELECT COUNT(*) FROM tpp_product_register", Integer.class));
    }

    @Test
    void skipsOccupiedAndLegacyReferencedAccounts() {
        service.create(request(9001));
        // Existing installations may contain a referenced account with a stale occupancy flag.
        jdbc.update("UPDATE account SET bussy = false WHERE id = 1");
        jdbc.update("UPDATE account SET bussy = true WHERE id = 2");
        service.create(request(9002));
        assertEquals(3L, jdbc.queryForObject(
                "SELECT account FROM tpp_product_register WHERE product_id = 9002", Long.class));
    }
}
