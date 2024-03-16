package ru.practice5.main.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practice5.main.dto.NewCorporateSettlementAccountDto;
import ru.practice5.main.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CsaServiceTests {

    private final CsaService service;

    @Test
    void shouldCreate() {
        var input = new NewCorporateSettlementAccountDto();
        input.instanceId = 1L;
        input.registryTypeCode = "03.012.002_47533_ComSoLd";
        var output = service.create(input);
        assertEquals(output.data.accountId, "1");
    }

    @Test
    void shouldNotCreateWithoutInstanceId() {
        var input = new NewCorporateSettlementAccountDto();
        input.registryTypeCode = "03.012.002_47533_ComSoLd";
        assertThrows(
                ValidationException.class,
                () -> service.create(input)
        );
    }
}