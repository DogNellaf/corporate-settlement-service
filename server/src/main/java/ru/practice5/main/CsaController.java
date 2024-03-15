package ru.practice5.main;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practice5.main.dto.NewCorporateSettlementAccountDto;
import ru.practice5.main.exception.ValidationException;
import ru.practice5.main.models.TppProduct;
import ru.practice5.main.repository.AgreementRepository;
import ru.practice5.main.repository.TppProductRepository;
import ru.practice5.main.repository.TppRefProductClassRepository;
import ru.practice5.main.repository.TppRefProductRegisterTypeRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/corporate-settlement-account")
public class CsaController {

    private final TppProductRepository tppProductRepository;
    private final TppRefProductClassRepository tppRefProductClassRepository;
    private final AgreementRepository agreementRepository;
    private final TppRefProductRegisterTypeRepository tppRefProductRegisterTypeRepository;

    // метод проверки входных данных
    private void validate(NewCorporateSettlementAccountDto dto) {

        // проверка всех обязательных полей, шаг 1

        if (dto.productType == null) {
            throw new ValidationException("productType <значение> не заполнено.");
        }
        if (dto.productCode == null) {
            throw new ValidationException("productCode <значение> не заполнено.");
        }
        if (dto.registerType == null) {
            throw new ValidationException("registerType <значение> не заполнено.");
        }
        if (dto.mdmCode == null) {
            throw new ValidationException("mdmCode <значение> не заполнено.");
        }
        if (dto.contractNumber == null) {
            throw new ValidationException("contractNumber <значение> не заполнено.");
        }
        if (dto.contractDate == null) {
            throw new ValidationException("contractDate <значение> не заполнено.");
        }
        if (dto.priority == null) {
            throw new ValidationException("priority <значение> не заполнено.");
        }
        if (dto.contractId == null) {
            throw new ValidationException("contractId <значение> не заполнено.");
        }
        if (dto.BranchCode == null) {
            throw new ValidationException("BranchCode <значение> не заполнено.");
        }
        if (dto.IsoCurrencyCode == null) {
            throw new ValidationException("IsoCurrencyCode <значение> не заполнено.");
        }
        if (dto.urgencyCode == null) {
            throw new ValidationException("urgencyCode <значение> не заполнено.");
        }

        // шаг 1.1
        if (dto.instanceId == null) {
            var products = tppProductRepository.findByNumber(dto.contractNumber);
            if (!products.isEmpty()) {
                var message = "Параметр ContractNumber № договора %s уже существует для ЭП с ИД %s.".formatted(products.get(0).getId(), dto.contractNumber);
                throw new ValidationException(message);
            }

            // шаг 1.2
            for (var number : dto.InstanceArrangementDto) {
                var agreements = agreementRepository.findByNumber(number.Number);
                if (!agreements.isEmpty()) {
                    var message = "Параметр № Дополнительного соглашения (сделки) Number %s уже существует для ЭП с ИД %s.".formatted(number.Number, dto.contractNumber);
                    throw new ValidationException(message);
                }
            }
            // шаг 1.3
            var registerTypes = tppRefProductRegisterTypeRepository.findByValue(dto.productCode);
            if (registerTypes.isEmpty()) {
                var message = "КодПродукта %s не найдено в Каталоге продуктов tpp_ref_product_class".formatted(dto.productCode);
                throw new ValidationException(message);
            }

            // шаг 1.4
            var product = TppProduct(

            )
            tppProductRepository.save()
        }

//        if (dto.InstanceArrangementDto != null) {
//            for (var element : dto.InstanceArrangementDto) {
//                if (element.Number == null) {
//                    throw new ValidationException("Отсутствует Number");
//                }
//                if (element.openingDate == null) {
//                    throw new ValidationException("Отсутствует openingDate");
//                }
//            }
//        }

    }

    @PostMapping("create")
    public void create(@RequestBody NewCorporateSettlementAccountDto input) {
        validate(input);

    }
}
