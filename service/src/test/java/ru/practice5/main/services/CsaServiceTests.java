package ru.practice5.main.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practice5.main.dto.NewCorporateSettlementAccountDto;
import ru.practice5.main.exception.NotFoundException;
import ru.practice5.main.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CsaServiceTests {

    private final CsaService service;

    @Test
    void shouldCreate() {
        var dto = new NewCorporateSettlementAccountDto();
        dto.setInstanceId(1L);
        dto.setRegistryTypeCode("03.012.002_47533_ComSoLd");
        dto.setBranchCode("0022");
        dto.setCurrencyCode("800");
        dto.setMdmCode("15");
        dto.setPriorityCode("00");
        var output = service.create(dto);
        assertNotNull(output.getData().getAccountId());
        assertTrue(Long.parseLong(output.getData().getAccountId()) > 0);
    }

    @Test
    void shouldNotCreateWithoutInstanceId() {
        var dto = new NewCorporateSettlementAccountDto();
        dto.setRegistryTypeCode("03.012.002_47533_ComSoLd");
        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    void shouldNotCreateWhenRegisterTypeAlreadyExists() {
        var dto = new NewCorporateSettlementAccountDto();
        dto.setInstanceId(1L);
        dto.setRegistryTypeCode("03.012.002_47533_ComSoLd");
        dto.setBranchCode("0022");
        dto.setCurrencyCode("800");
        dto.setMdmCode("15");
        dto.setPriorityCode("00");
        service.create(dto);
        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    void shouldNotCreateWhenRegisterTypeNotFound() {
        var dto = new NewCorporateSettlementAccountDto();
        dto.setInstanceId(1L);
        dto.setRegistryTypeCode("NONEXISTENT_TYPE");
        assertThrows(NotFoundException.class, () -> service.create(dto));
    }

    @Test
    void shouldNotCreateWhenAccountPoolNotFound() {
        var dto = new NewCorporateSettlementAccountDto();
        dto.setInstanceId(1L);
        dto.setRegistryTypeCode("03.012.002_47533_ComSoLd");
        dto.setBranchCode("9999");
        dto.setCurrencyCode("999");
        dto.setMdmCode("99");
        dto.setPriorityCode("99");
        assertThrows(NotFoundException.class, () -> service.create(dto));
    }

    @Test
    void shouldCreateWithNullFilters() {
        var dto = new NewCorporateSettlementAccountDto();
        dto.setInstanceId(2L);
        dto.setRegistryTypeCode("03.012.002_47533_ComSoLd");
        var output = service.create(dto);
        assertNotNull(output.getData().getAccountId());
    }
}
