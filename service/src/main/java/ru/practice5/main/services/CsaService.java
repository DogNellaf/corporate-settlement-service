package ru.practice5.main.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practice5.main.dto.csa.CsaOutputDto;
import ru.practice5.main.dto.csa.CsaOutputParam;
import ru.practice5.main.dto.NewCorporateSettlementAccountDto;
import ru.practice5.main.exception.NotFoundException;
import ru.practice5.main.exception.ValidationException;
import ru.practice5.main.models.TppProductRegister;
import ru.practice5.main.repository.AccountPoolRepository;
import ru.practice5.main.repository.AccountRepository;
import ru.practice5.main.repository.TppProductRegisterRepository;
import ru.practice5.main.repository.TppRefProductRegisterTypeRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CsaService {

    private final TppProductRegisterRepository tppProductRegisterRepository;
    private final TppRefProductRegisterTypeRepository tppRefProductRegisterTypeRepository;
    private final AccountPoolRepository accountPoolRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public CsaOutputDto create(NewCorporateSettlementAccountDto dto) {

        // проверка обязательных полей, шаг 1
        if (dto.getInstanceId() == null) {
            throw new ValidationException("Имя обязательного параметра instanceId не заполнено.");
        }

        // шаг 2
        var registers = tppProductRegisterRepository.findAllByProductIdAndType(dto.instanceId, dto.registryTypeCode);
        if (!registers.isEmpty()) {
            var message = "Параметр registryTypeCode тип регистра"+ dto.registryTypeCode +" уже существует для ЭП с ИД " + dto.instanceId.toString();
            throw new ValidationException(message);
        }

        // шаг 3
        var type = tppRefProductRegisterTypeRepository.findOneByValue(dto.registryTypeCode);

        if (type.isEmpty()) {
            var message = "Код Продукта " + dto.instanceId.toString() + " не найдено в Каталоге продуктов public.tpp_ref_product_register_type для данного типа Регистра.";
            throw new NotFoundException(message);
        }

        // шаг 4
        var accounts_pool = accountPoolRepository.findAll();
        if (dto.branchCode != null) {
            accounts_pool = accounts_pool.stream().filter(x -> x.getBranch_code().equals(dto.branchCode)).collect(Collectors.toList());
        }

        if (dto.currencyCode != null) {
            accounts_pool = accounts_pool.stream().filter(x -> x.getCurrency_code().equals(dto.currencyCode)).collect(Collectors.toList());
        }

        if (dto.mdmCode != null) {
            accounts_pool = accounts_pool.stream().filter(x -> x.getMdm_code().equals(dto.mdmCode)).collect(Collectors.toList());
        }

        if (dto.priorityCode != null) {
            accounts_pool = accounts_pool.stream().filter(x -> x.getPriority_code().equals(dto.priorityCode)).collect(Collectors.toList());
        }

        if (dto.registryTypeCode != null) {
            accounts_pool = accounts_pool.stream().filter(x -> x.getRegistry_type_code().equals(dto.registryTypeCode)).collect(Collectors.toList());
        }

        var account_pool = accounts_pool.get(0);
        var account = accountRepository.findOneByAccountPoolId(account_pool.getId());
        var entity = new TppProductRegister(
                -1L,
                dto.instanceId,
                type.stream().findFirst().get().getValue(),
                account.getId(),
                dto.currencyCode,
                "OPEN",
                account.getAccount_number()
        );
        entity = tppProductRegisterRepository.save(entity);
        return new CsaOutputDto(
                new CsaOutputParam(
                        entity.getId().toString()
                )
        );
    }
}
