package ru.practice5.main.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practice5.main.dto.InstanceArrangementDto;
import ru.practice5.main.dto.NewCorporateSettlementInstanceDto;
import ru.practice5.main.exception.NotFoundException;
import ru.practice5.main.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CsiServiceTests {

    private final CsiService service;

    private NewCorporateSettlementInstanceDto validDto() {
        var dto = new NewCorporateSettlementInstanceDto();
        dto.setProductType("CORPORATE");
        dto.setProductCode("03.012.002");
        dto.setRegisterType("03.012.002_47533_ComSoLd");
        dto.setMdmCode("15");
        dto.setContractNumber("CONTRACT-NEW-001");
        dto.setContractDate(LocalDateTime.now());
        dto.setPriority(1);
        dto.setContractId(0);
        dto.setBranchCode("0022");
        dto.setIsoCurrencyCode("800");
        dto.setUrgencyCode("NORMAL");
        return dto;
    }

    @Test
    void shouldCreateNewInstance() {
        var result = service.create(validDto());
        assertNotNull(result.getData().getInstanceId());
        assertTrue(Long.parseLong(result.getData().getInstanceId()) > 0);
        assertNotNull(result.getRegisterId());
        assertEquals(1, result.getRegisterId().size());
        assertNull(result.getSupplementaryAgreementId());
    }

    @Test
    void shouldNotCreateWithoutProductType() {
        var dto = validDto();
        dto.setProductType(null);
        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    void shouldNotCreateWithoutProductCode() {
        var dto = validDto();
        dto.setProductCode(null);
        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    void shouldNotCreateWithoutRegisterType() {
        var dto = validDto();
        dto.setRegisterType(null);
        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    void shouldNotCreateWithoutMdmCode() {
        var dto = validDto();
        dto.setMdmCode(null);
        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    void shouldNotCreateWithoutContractNumber() {
        var dto = validDto();
        dto.setContractNumber(null);
        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    void shouldNotCreateWithoutContractDate() {
        var dto = validDto();
        dto.setContractDate(null);
        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    void shouldNotCreateWithoutPriority() {
        var dto = validDto();
        dto.setPriority(null);
        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    void shouldNotCreateWithoutContractId() {
        var dto = validDto();
        dto.setContractId(null);
        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    void shouldNotCreateWithoutBranchCode() {
        var dto = validDto();
        dto.setBranchCode(null);
        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    void shouldNotCreateWithoutIsoCurrencyCode() {
        var dto = validDto();
        dto.setIsoCurrencyCode(null);
        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    void shouldNotCreateWithoutUrgencyCode() {
        var dto = validDto();
        dto.setUrgencyCode(null);
        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    void shouldNotCreateWhenContractNumberAlreadyExists() {
        var dto = validDto();
        dto.setContractNumber("P001");
        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    void shouldNotCreateWhenAgreementNumberAlreadyExists() {
        var dto = validDto();
        var arrangement = new InstanceArrangementDto();
        arrangement.setNumber("Agreement001");
        dto.setInstanceArrangementDto(List.of(arrangement));
        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    void shouldNotCreateWhenProductCodeNotInCatalog() {
        var dto = validDto();
        dto.setProductCode("NONEXISTENT");
        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    void shouldUpdateExistingInstanceWithArrangements() {
        var arrangement = new InstanceArrangementDto();
        arrangement.setNumber("NEW-AGREEMENT-001");
        arrangement.setGeneralAgreementId("GA-001");
        arrangement.setSupplementaryAgreementId("SA-001");
        arrangement.setArrangementType("TYPE-A");
        arrangement.setSchedulerJobId(1);
        arrangement.setOpeningDate(LocalDateTime.now());
        arrangement.setValidityDuration(365);

        var dto = validDto();
        dto.setInstanceId(1);
        dto.setInstanceArrangementDto(List.of(arrangement));

        var result = service.create(dto);
        assertEquals("1", result.getData().getInstanceId());
        assertNull(result.getRegisterId());
        assertNotNull(result.getSupplementaryAgreementId());
        assertEquals(1, result.getSupplementaryAgreementId().size());
    }

    @Test
    void shouldUpdateExistingInstanceWithNoArrangements() {
        var dto = validDto();
        dto.setInstanceId(1);

        var result = service.create(dto);
        assertEquals("1", result.getData().getInstanceId());
        assertNull(result.getRegisterId());
        assertNotNull(result.getSupplementaryAgreementId());
        assertTrue(result.getSupplementaryAgreementId().isEmpty());
    }

    @Test
    void shouldNotUpdateWhenInstanceNotFound() {
        var arrangement = new InstanceArrangementDto();
        arrangement.setNumber("NEW-AGREEMENT-999");

        var dto = validDto();
        dto.setInstanceId(9999);
        dto.setInstanceArrangementDto(List.of(arrangement));

        assertThrows(NotFoundException.class, () -> service.create(dto));
    }

    @Test
    void shouldNotUpdateWhenAgreementNumberAlreadyExistsForInstance() {
        var arrangement = new InstanceArrangementDto();
        arrangement.setNumber("Agreement001");

        var dto = validDto();
        dto.setInstanceId(1);
        dto.setInstanceArrangementDto(List.of(arrangement));

        assertThrows(ValidationException.class, () -> service.create(dto));
    }
}
