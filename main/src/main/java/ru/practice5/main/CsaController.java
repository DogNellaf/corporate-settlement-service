package ru.practice5.main;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practice5.main.dto.CsaOutputDto;
import ru.practice5.main.dto.CsaOutputParam;
import ru.practice5.main.dto.NewCorporateSettlementAccountDto;
import ru.practice5.main.exception.NotFoundException;
import ru.practice5.main.exception.ValidationException;
import ru.practice5.main.models.TppProductRegister;
import ru.practice5.main.repository.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/corporate-settlement-account")
@Transactional
public class CsaController {

    private final TppProductRegisterRepository tppProductRegisterRepository;
    private final TppRefProductRegisterTypeRepository tppRefProductRegisterTypeRepository;
    private final AccountPoolRepository accountPoolRepository;
    private final AccountRepository accountRepository;

    @PostMapping("create")
    public CsaOutputDto create(@RequestBody NewCorporateSettlementAccountDto dto) {

        // проверка обязательных полей, шаг 1
        if (dto.getInstanceId() == null) {
            throw new ValidationException("Имя обязательного параметра instanceId не заполнено.");
        }

        // шаг 2
        var registers = tppProductRegisterRepository.findAllByProductIdAndType(dto.instanceId, dto.registryTypeCode);
        if (!registers.isEmpty()) {
            var message = "Параметр registryTypeCode тип регистра %s уже существует для ЭП с ИД %s.".formatted(dto.registryTypeCode, dto.instanceId);
            throw new ValidationException(message);
        }

        // шаг 3
        var type = tppRefProductRegisterTypeRepository.findOneByValue(dto.registryTypeCode);

        if (type.isEmpty()) {
            var message = "Код Продукта %s не найдено в Каталоге продуктов public.tpp_ref_product_register_type для данного типа Регистра.".formatted(dto.instanceId);
            throw new NotFoundException(message);
        }

        // шаг 4
        var accounts_pool = accountPoolRepository.findAll().stream();
        if (dto.getBranchCode() != null) {
            accounts_pool = accounts_pool.filter(x -> x.getBranch_code().equals(dto.branchCode));
        }

        if (dto.currencyCode != null) {
            accounts_pool = accounts_pool.filter(x -> x.getCurrency_code().equals(dto.currencyCode));
        }

        if (dto.mdmCode != null) {
            accounts_pool = accounts_pool.filter(x -> x.getMdm_code().equals(dto.mdmCode));
        }

        if (dto.priorityCode != null) {
            accounts_pool = accounts_pool.filter(x -> x.getPriority_code().equals(dto.priorityCode));
        }

        if (dto.registryTypeCode != null) {
            accounts_pool = accounts_pool.filter(x -> x.getRegistry_type_code().equals(dto.registryTypeCode));
        }

        var account_pool = accounts_pool.findFirst().get();
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
