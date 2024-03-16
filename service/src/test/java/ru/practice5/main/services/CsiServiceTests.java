package ru.practice5.main.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practice5.main.dto.NewCorporateSettlementInstanceDto;
import ru.practice5.main.exception.ValidationException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CsiServiceTests {

    private final CsiService service;

    @Test
    void shouldCreate() {
        var dto = new NewCorporateSettlementInstanceDto();
        dto.productType = "";
        dto.productCode = "03.012.002";
        dto.registerType = "03.012.002_47533_ComSoLd";
        dto.mdmCode = "15";
        dto.contractNumber = "";
        dto.contractDate = LocalDateTime.now();
        dto.priority = 0;
        dto.contractId = 0;
        dto.BranchCode = "0022";
        dto.IsoCurrencyCode = "800";
        dto.urgencyCode = "";
        var result = service.create(dto);
        assertEquals(result.data.instanceId, "3");
        assertEquals(result.registerId.size(), 1);
        assertEquals(result.registerId.get(0), "2");
        assertNull(result.supplementaryAgreementId);
    }

    @Test
    void shouldNotCreateWithoutProductType() {
        var dto = new NewCorporateSettlementInstanceDto();
        dto.productCode = "03.012.002";
        dto.registerType = "03.012.002_47533_ComSoLd";
        dto.mdmCode = "15";
        dto.contractNumber = "";
        dto.contractDate = LocalDateTime.now();
        dto.priority = 0;
        dto.contractId = 0;
        dto.BranchCode = "0022";
        dto.IsoCurrencyCode = "800";
        dto.urgencyCode = "";
        assertThrows(
                ValidationException.class,
                () -> service.create(dto)
        );
    }

    @Test
    void shouldNotCreateWithoutRegisterType() {
        var dto = new NewCorporateSettlementInstanceDto();
        dto.productType = "";
        dto.productCode = "03.012.002";
        dto.mdmCode = "15";
        dto.contractNumber = "";
        dto.contractDate = LocalDateTime.now();
        dto.priority = 0;
        dto.contractId = 0;
        dto.BranchCode = "0022";
        dto.IsoCurrencyCode = "800";
        dto.urgencyCode = "";
        assertThrows(
                ValidationException.class,
                () -> service.create(dto)
        );
    }

    @Test
    void shouldNotCreateWithoutProductCode() {
        var dto = new NewCorporateSettlementInstanceDto();
        dto.productType = "";
        dto.registerType = "03.012.002_47533_ComSoLd";
        dto.mdmCode = "15";
        dto.contractNumber = "";
        dto.contractDate = LocalDateTime.now();
        dto.priority = 0;
        dto.contractId = 0;
        dto.BranchCode = "0022";
        dto.IsoCurrencyCode = "800";
        dto.urgencyCode = "";
        assertThrows(
                ValidationException.class,
                () -> service.create(dto)
        );
    }
}