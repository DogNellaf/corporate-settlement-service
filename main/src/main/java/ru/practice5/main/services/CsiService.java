package ru.practice5.main.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practice5.main.dto.*;
import ru.practice5.main.exception.ValidationException;
import ru.practice5.main.models.TppProduct;
import ru.practice5.main.repository.*;

@Service
@RequiredArgsConstructor
public class CsiService {

    private final TppProductRepository tppProductRepository;
    private final AgreementRepository agreementRepository;
    private final TppRefProductRegisterTypeRepository tppRefProductRegisterTypeRepository;
//    private final AccountRepository accountRepository;

    public CsiOutputDto create(NewCorporateSettlementInstanceDto dto) {

        // проверка всех обязательных полей, шаг 1

        if (dto.productType == null) {
            throw new ValidationException("Имя обязательного параметра productType не заполнено.");
        }

        if (dto.productCode == null) {
            throw new ValidationException("Имя обязательного параметра productCode не заполнено.");
        }

        if (dto.registerType == null) {
            throw new ValidationException("Имя обязательного параметра registerType не заполнено.");
        }

        if (dto.mdmCode == null) {
            throw new ValidationException("Имя обязательного параметра mdmCode не заполнено.");
        }

        if (dto.contractNumber == null) {
            throw new ValidationException("Имя обязательного параметра contractNumber не заполнено.");
        }

        if (dto.contractDate == null) {
            throw new ValidationException("Имя обязательного параметра contractDate не заполнено.");
        }

        if (dto.priority == null) {
            throw new ValidationException("Имя обязательного параметра priority не заполнено.");
        }

        if (dto.contractId == null) {
            throw new ValidationException("Имя обязательного параметра contractId не заполнено.");
        }

        if (dto.BranchCode == null) {
            throw new ValidationException("Имя обязательного параметра BranchCode не заполнено.");
        }

        if (dto.IsoCurrencyCode == null) {
            throw new ValidationException("Имя обязательного параметра IsoCurrencyCode не заполнено.");
        }

        if (dto.urgencyCode == null) {
            throw new ValidationException("Имя обязательного параметра urgencyCode не заполнено.");
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
//            var product = new TppProduct();
//            product.
//                    tppProductRepository.save()
            return null;
        }
        return null;
    }
}
