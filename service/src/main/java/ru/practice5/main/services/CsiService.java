package ru.practice5.main.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practice5.main.dto.*;
import ru.practice5.main.dto.csi.CsiOutputDataParam;
import ru.practice5.main.dto.csi.CsiOutputDto;
import ru.practice5.main.exception.NotFoundException;
import ru.practice5.main.exception.ValidationException;
import ru.practice5.main.models.Agreement;
import ru.practice5.main.models.TppProduct;
import ru.practice5.main.repository.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CsiService {

    private final TppProductRepository tppProductRepository;
    private final AgreementRepository agreementRepository;
    private final TppRefProductRegisterTypeRepository tppRefProductRegisterTypeRepository;
    private final CsaService csaService;

    @Transactional
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
                var message = "Параметр ContractNumber № договора " + products.get(0).getId().toString() + " уже существует для ЭП с ИД " + dto.contractNumber;
                throw new ValidationException(message);
            }

            // шаг 1.2
            if (dto.getInstanceArrangementDto() != null) {
                for (var number : dto.InstanceArrangementDto) {
                    var agreements = agreementRepository.findByNumber(number.Number);
                    if (!agreements.isEmpty()) {
                        var message = "Параметр № Дополнительного соглашения (сделки) Number " + number.Number + " уже существует для ЭП с ИД " + dto.contractNumber;
                        throw new ValidationException(message);
                    }
                }
            }

            // шаг 1.3
            var registerTypes = tppRefProductRegisterTypeRepository.findByParams(dto.productCode);
            if (registerTypes.isEmpty()) {
                var message = "КодПродукта " + dto.productCode + " не найдено в Каталоге продуктов tpp_ref_product_class";
                throw new ValidationException(message);
            }

            // шаг 1.4
            var product = new TppProduct();
            product.setId(-1L);
            product.setProduct_code_id(dto.contractId);
            //client?
            product.setType(registerTypes.get(0).getValue());
            product.setNumber(dto.contractNumber);
            product.setPriority(dto.priority);
            // ? product.setDate_of_conclusion();
            product.setStart_date_time(dto.contractDate);
            // ? end_date_time
            // ? days
            product.setPenalty_rate(dto.interestRatePenalty);
            // ? product.setNso(dto.nso);
            product.setThreshold_amount(dto.thresholdAmount);
            // ? product.setRequisite_type
            // ? product.setInterest_rate_type();
            product.setTax_rate(dto.taxPercentageRate);
            // ? product.setReasone_close();
            // ? product.setState()
            product = tppProductRepository.save(product);
//            var product_id = product.getId();

            // шаг 1.5
            var accounts_ids = new ArrayList<String>();
            for (var type : registerTypes) {
                var account_id = csaService.create(new NewCorporateSettlementAccountDto(
                        product.getId(),
                        type.getValue(),
                        null,
                        dto.getIsoCurrencyCode(),
                        dto.getBranchCode(),
                        "00",
                        dto.mdmCode,
                        null,
                        null,
                        null,
                        null
                )).data.accountId;
                accounts_ids.add(account_id);
            }

            return new CsiOutputDto(
                    new CsiOutputDataParam(product.getId().toString()),
                    accounts_ids,
                    null
            );
        }

        // шаг 2.1

        var products = tppProductRepository.findByNumber(dto.contractNumber);

        if (products.isEmpty()) {
            var message = "Экземпляр продукта с параметром instanceId " + dto.contractNumber + " не найден";
            throw new NotFoundException(message);
        }

        var product = products.get(0);

        // шаг 2.2

        var numbers = dto.getInstanceArrangementDto().stream().map(x -> x.Number).collect(Collectors.toList());

        for (var arrangement : agreementRepository.findAllByNumberIn(numbers)) {
            var message = "Параметр № Дополнительного соглашения (сделки) Number " + arrangement.getNumber() + " уже существует для ЭП с ИД " + dto.getInstanceId().toString();
            throw new ValidationException(message);
        }

        // шаг 2.3 (8)

        var arrangements = new ArrayList<String>();

        for (var arrangement : dto.getInstanceArrangementDto()) {
            var entity = new Agreement(
                    -1L,
                    product.getId().toString(),
                    arrangement.getGeneralAgreementId(),
                    arrangement.getSupplementaryAgreementId(),
                    arrangement.getArrangementType(),
                    arrangement.getShedulerJobId().longValue(),
                    arrangement.getNumber(),
                    arrangement.getOpeningDate(),
                    arrangement.getClosingDate(),
                    arrangement.getCancelDate(),
                    arrangement.getValidityDuration().longValue(),
                    arrangement.getCancellationReason(),
                    arrangement.getStatus(),
                    arrangement.getInterestCalculationDate(),
                    arrangement.getInterestRate(),
                    arrangement.getCoefficient(),
                    arrangement.getCoefficientAction(),
                    arrangement.getMinimumInterestRate(),
                    arrangement.getMaximalnterestRateCoefficient(),
                    arrangement.minimumInterestRateCoefficientAction,
                    arrangement.getMaximalnterestRate(),
                    arrangement.maximalnterestRateCoefficient,
                    arrangement.minimumInterestRateCoefficientAction
            );
            entity = agreementRepository.save(entity);
            arrangements.add(entity.getId().toString());
        }

        return new CsiOutputDto(
                new CsiOutputDataParam(dto.instanceId.toString()),
                null,
                arrangements
        );
    }
}
