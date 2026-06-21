package ru.practice5.main.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.practice5.main.dto.InstanceArrangementDto;
import ru.practice5.main.dto.NewCorporateSettlementInstanceDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CsiControllerTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private NewCorporateSettlementInstanceDto validDto() {
        var dto = new NewCorporateSettlementInstanceDto();
        dto.setProductType("CORPORATE");
        dto.setProductCode("03.012.002");
        dto.setRegisterType("03.012.002_47533_ComSoLd");
        dto.setMdmCode("15");
        dto.setContractNumber("CONTRACT-HTTP-001");
        dto.setContractDate(LocalDateTime.now());
        dto.setPriority(1);
        dto.setContractId(0);
        dto.setBranchCode("0022");
        dto.setIsoCurrencyCode("800");
        dto.setUrgencyCode("NORMAL");
        return dto;
    }

    @Test
    void shouldReturn200OnCreateNewInstance() throws Exception {
        mockMvc.perform(post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.instanceId").isNotEmpty())
                .andExpect(jsonPath("$.registerId").isArray());
    }

    @Test
    void shouldReturn400WhenRequiredFieldMissing() throws Exception {
        var dto = validDto();
        dto.setProductType(null);

        mockMvc.perform(post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    void shouldReturn400WhenContractNumberAlreadyExists() throws Exception {
        var dto = validDto();
        dto.setContractNumber("P001");

        mockMvc.perform(post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    void shouldReturn200OnUpdateExistingInstance() throws Exception {
        var arrangement = new InstanceArrangementDto();
        arrangement.setNumber("NEW-ARRANGEMENT-HTTP-001");
        arrangement.setArrangementType("TYPE-A");
        arrangement.setSchedulerJobId(1);
        arrangement.setValidityDuration(365);
        arrangement.setOpeningDate(LocalDateTime.now());

        var dto = validDto();
        dto.setInstanceId(1);
        dto.setInstanceArrangementDto(List.of(arrangement));

        mockMvc.perform(post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.instanceId").value("1"))
                .andExpect(jsonPath("$.supplementaryAgreementId").isArray());
    }

    @Test
    void shouldReturn404WhenInstanceIdNotFound() throws Exception {
        var arrangement = new InstanceArrangementDto();
        arrangement.setNumber("NEW-ARRANGEMENT-HTTP-002");

        var dto = validDto();
        dto.setInstanceId(9999);
        dto.setInstanceArrangementDto(List.of(arrangement));

        mockMvc.perform(post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").isNotEmpty());
    }
}
