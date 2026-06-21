package ru.practice5.main.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practice5.main.dto.NewCorporateSettlementAccountDto;
import ru.practice5.main.dto.csa.CsaOutputDto;
import ru.practice5.main.dto.csa.CsaOutputParam;
import ru.practice5.main.exception.NotFoundException;
import ru.practice5.main.exception.ValidationException;
import ru.practice5.main.models.TppProductRegister;
import ru.practice5.main.repository.AccountPoolRepository;
import ru.practice5.main.repository.AccountRepository;
import ru.practice5.main.repository.TppProductRegisterRepository;
import ru.practice5.main.repository.TppRefProductRegisterTypeRepository;

@Service
@RequiredArgsConstructor
public class CsaService {

    private final TppProductRegisterRepository tppProductRegisterRepository;
    private final TppRefProductRegisterTypeRepository tppRefProductRegisterTypeRepository;
    private final AccountPoolRepository accountPoolRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public CsaOutputDto create(NewCorporateSettlementAccountDto dto) {
        if (dto.getInstanceId() == null) {
            throw new ValidationException("Имя обязательного параметра instanceId не заполнено.");
        }

        var registers = tppProductRegisterRepository
                .findAllByProductIdAndType(dto.getInstanceId(), dto.getRegistryTypeCode());
        if (!registers.isEmpty()) {
            throw new ValidationException(
                    "Параметр registryTypeCode тип регистра " + dto.getRegistryTypeCode()
                            + " уже существует для ЭП с ИД " + dto.getInstanceId());
        }

        var registerType = tppRefProductRegisterTypeRepository.findOneByValue(dto.getRegistryTypeCode());
        if (registerType.isEmpty()) {
            throw new NotFoundException(
                    "Код Продукта " + dto.getInstanceId()
                            + " не найдено в Каталоге продуктов public.tpp_ref_product_register_type"
                            + " для данного типа Регистра.");
        }

        var accountPools = accountPoolRepository.findByParams(
                dto.getBranchCode(),
                dto.getCurrencyCode(),
                dto.getMdmCode(),
                dto.getPriorityCode(),
                dto.getRegistryTypeCode()
        );
        if (accountPools.isEmpty()) {
            throw new NotFoundException(
                    "Подходящий пул счетов не найден для указанных параметров.");
        }

        var accountPool = accountPools.get(0);
        var account = accountRepository.findOneByAccountPoolId(accountPool.getId());
        if (account == null) {
            throw new NotFoundException(
                    "Счёт не найден в пуле с ИД " + accountPool.getId() + ".");
        }

        var entity = new TppProductRegister(
                null,
                dto.getInstanceId(),
                registerType.get().getValue(),
                account.getId(),
                dto.getCurrencyCode(),
                "OPEN",
                account.getAccountNumber()
        );
        entity = tppProductRegisterRepository.save(entity);
        return new CsaOutputDto(new CsaOutputParam(entity.getId().toString()));
    }
}
