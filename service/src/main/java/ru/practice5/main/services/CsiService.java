package ru.practice5.main.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practice5.main.dto.InstanceArrangementDto;
import ru.practice5.main.dto.NewCorporateSettlementAccountDto;
import ru.practice5.main.dto.NewCorporateSettlementInstanceDto;
import ru.practice5.main.dto.csi.CsiOutputDataParam;
import ru.practice5.main.dto.csi.CsiOutputDto;
import ru.practice5.main.exception.NotFoundException;
import ru.practice5.main.exception.ValidationException;
import ru.practice5.main.models.Agreement;
import ru.practice5.main.models.TppProduct;
import ru.practice5.main.repository.AgreementRepository;
import ru.practice5.main.repository.TppProductRepository;
import ru.practice5.main.repository.TppRefProductRegisterTypeRepository;

import java.util.ArrayList;
import java.util.List;
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
        validateRequiredFields(dto);

        if (dto.getInstanceId() == null) {
            return createNewInstance(dto);
        }
        return updateExistingInstance(dto);
    }

    private void validateRequiredFields(NewCorporateSettlementInstanceDto dto) {
        if (dto.getProductType() == null) {
            throw new ValidationException("Имя обязательного параметра productType не заполнено.");
        }
        if (dto.getProductCode() == null) {
            throw new ValidationException("Имя обязательного параметра productCode не заполнено.");
        }
        if (dto.getRegisterType() == null) {
            throw new ValidationException("Имя обязательного параметра registerType не заполнено.");
        }
        if (dto.getMdmCode() == null) {
            throw new ValidationException("Имя обязательного параметра mdmCode не заполнено.");
        }
        if (dto.getContractNumber() == null) {
            throw new ValidationException("Имя обязательного параметра contractNumber не заполнено.");
        }
        if (dto.getContractDate() == null) {
            throw new ValidationException("Имя обязательного параметра contractDate не заполнено.");
        }
        if (dto.getPriority() == null) {
            throw new ValidationException("Имя обязательного параметра priority не заполнено.");
        }
        if (dto.getContractId() == null) {
            throw new ValidationException("Имя обязательного параметра contractId не заполнено.");
        }
        if (dto.getBranchCode() == null) {
            throw new ValidationException("Имя обязательного параметра branchCode не заполнено.");
        }
        if (dto.getIsoCurrencyCode() == null) {
            throw new ValidationException("Имя обязательного параметра isoCurrencyCode не заполнено.");
        }
        if (dto.getUrgencyCode() == null) {
            throw new ValidationException("Имя обязательного параметра urgencyCode не заполнено.");
        }
    }

    private CsiOutputDto createNewInstance(NewCorporateSettlementInstanceDto dto) {
        var existingProducts = tppProductRepository.findByNumber(dto.getContractNumber());
        if (!existingProducts.isEmpty()) {
            throw new ValidationException(
                    "Параметр ContractNumber № договора " + existingProducts.get(0).getId()
                            + " уже существует для ЭП с ИД " + dto.getContractNumber());
        }

        if (dto.getInstanceArrangementDto() != null) {
            for (InstanceArrangementDto arrangement : dto.getInstanceArrangementDto()) {
                var agreements = agreementRepository.findByNumber(arrangement.getNumber());
                if (!agreements.isEmpty()) {
                    throw new ValidationException(
                            "Параметр № Дополнительного соглашения (сделки) Number "
                                    + arrangement.getNumber()
                                    + " уже существует для ЭП с ИД " + dto.getContractNumber());
                }
            }
        }

        var registerTypes = tppRefProductRegisterTypeRepository.findByParams(dto.getProductCode());
        if (registerTypes.isEmpty()) {
            throw new ValidationException(
                    "КодПродукта " + dto.getProductCode()
                            + " не найдено в Каталоге продуктов tpp_ref_product_class");
        }

        var product = new TppProduct();
        product.setProductCodeId(dto.getContractId().longValue());
        product.setType(registerTypes.get(0).getValue());
        product.setNumber(dto.getContractNumber());
        product.setPriority(dto.getPriority());
        product.setStartDateTime(dto.getContractDate());
        product.setPenaltyRate(dto.getInterestRatePenalty());
        product.setThresholdAmount(dto.getThresholdAmount());
        product.setTaxRate(dto.getTaxPercentageRate());
        product = tppProductRepository.save(product);

        var accountIds = new ArrayList<String>();
        for (var type : registerTypes) {
            var accountId = csaService.create(new NewCorporateSettlementAccountDto(
                    product.getId(),
                    type.getValue(),
                    null,
                    dto.getIsoCurrencyCode(),
                    dto.getBranchCode(),
                    "00",
                    dto.getMdmCode(),
                    null,
                    null,
                    null,
                    null
            )).getData().getAccountId();
            accountIds.add(accountId);
        }

        return new CsiOutputDto(
                new CsiOutputDataParam(product.getId().toString()),
                accountIds,
                null
        );
    }

    private CsiOutputDto updateExistingInstance(NewCorporateSettlementInstanceDto dto) {
        var product = tppProductRepository.findById(dto.getInstanceId().longValue())
                .orElseThrow(() -> new NotFoundException(
                        "Экземпляр продукта с параметром instanceId " + dto.getInstanceId() + " не найден"));

        List<InstanceArrangementDto> arrangements = dto.getInstanceArrangementDto();
        if (arrangements == null || arrangements.isEmpty()) {
            return new CsiOutputDto(
                    new CsiOutputDataParam(dto.getInstanceId().toString()),
                    null,
                    new ArrayList<>()
            );
        }

        var numbers = arrangements.stream()
                .map(InstanceArrangementDto::getNumber)
                .collect(Collectors.toList());

        for (var existing : agreementRepository.findAllByNumberIn(numbers)) {
            throw new ValidationException(
                    "Параметр № Дополнительного соглашения (сделки) Number " + existing.getNumber()
                            + " уже существует для ЭП с ИД " + dto.getInstanceId());
        }

        var agreementIds = new ArrayList<String>();
        for (InstanceArrangementDto arrangement : arrangements) {
            var entity = new Agreement(
                    null,
                    product.getId(),
                    arrangement.getGeneralAgreementId(),
                    arrangement.getSupplementaryAgreementId(),
                    arrangement.getArrangementType(),
                    arrangement.getSchedulerJobId() != null
                            ? arrangement.getSchedulerJobId().longValue() : null,
                    arrangement.getNumber(),
                    arrangement.getOpeningDate(),
                    arrangement.getClosingDate(),
                    arrangement.getCancelDate(),
                    arrangement.getValidityDuration() != null
                            ? arrangement.getValidityDuration().longValue() : null,
                    arrangement.getCancellationReason(),
                    arrangement.getStatus(),
                    arrangement.getInterestCalculationDate(),
                    arrangement.getInterestRate(),
                    arrangement.getCoefficient(),
                    arrangement.getCoefficientAction(),
                    arrangement.getMinimumInterestRate(),
                    arrangement.getMinimumInterestRateCoefficient(),
                    arrangement.getMinimumInterestRateCoefficientAction(),
                    arrangement.getMaximalInterestRate(),
                    arrangement.getMaximalInterestRateCoefficient(),
                    arrangement.getMaximalInterestRateCoefficientAction()
            );
            entity = agreementRepository.save(entity);
            agreementIds.add(entity.getId().toString());
        }

        return new CsiOutputDto(
                new CsiOutputDataParam(dto.getInstanceId().toString()),
                null,
                agreementIds
        );
    }
}
