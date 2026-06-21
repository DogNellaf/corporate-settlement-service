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
import ru.practice5.main.dto.NewCorporateSettlementAccountDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CsaControllerTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    void shouldReturn200OnCreate() throws Exception {
        var dto = new NewCorporateSettlementAccountDto();
        dto.setInstanceId(1L);
        dto.setRegistryTypeCode("03.012.002_47533_ComSoLd");
        dto.setBranchCode("0022");
        dto.setCurrencyCode("800");
        dto.setMdmCode("15");
        dto.setPriorityCode("00");

        mockMvc.perform(post("/corporate-settlement-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accountId").isNotEmpty());
    }

    @Test
    void shouldReturn400WhenInstanceIdMissing() throws Exception {
        var dto = new NewCorporateSettlementAccountDto();
        dto.setRegistryTypeCode("03.012.002_47533_ComSoLd");

        mockMvc.perform(post("/corporate-settlement-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    void shouldReturn404WhenRegisterTypeNotFound() throws Exception {
        var dto = new NewCorporateSettlementAccountDto();
        dto.setInstanceId(1L);
        dto.setRegistryTypeCode("NONEXISTENT_TYPE");

        mockMvc.perform(post("/corporate-settlement-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    void shouldReturn400WhenRegisterTypeAlreadyExists() throws Exception {
        var dto = new NewCorporateSettlementAccountDto();
        dto.setInstanceId(1L);
        dto.setRegistryTypeCode("03.012.002_47533_ComSoLd");
        dto.setBranchCode("0022");
        dto.setCurrencyCode("800");
        dto.setMdmCode("15");
        dto.setPriorityCode("00");

        mockMvc.perform(post("/corporate-settlement-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/corporate-settlement-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
