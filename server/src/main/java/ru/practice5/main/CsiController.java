package ru.practice5.main;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practice5.main.dto.NewCorporateSettlementAccountDto;
import ru.practice5.main.exception.ValidationException;
import ru.practice5.main.service.CsiService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/corporate-settlement-instance")
public class CsiController {

    private final CsiService service;

//    private void validate(NewCorporateSettlementAccountDto dto) {
//        if (dto.productType == null) {
//            throw new ValidationException("Отсутствует productType");
//        }
//        if (dto.productCode == null) {
//            throw new ValidationException("Отсутствует productCode");
//        }
//        if (dto.registerType == null) {
//            throw new ValidationException("Отсутствует registerType");
//        }
//        if (dto.mdmCode == null) {
//            throw new ValidationException("Отсутствует mdmCode");
//        }
//        if (dto.contractNumber == null) {
//            throw new ValidationException("Отсутствует contractNumber");
//        }
//        if (dto.contractDate == null) {
//            throw new ValidationException("Отсутствует contractDate");
//        }
//        if (dto.priority == null) {
//            throw new ValidationException("Отсутствует priority");
//        }
//        if (dto.contractId == null) {
//            throw new ValidationException("Отсутствует contractId");
//        }
//        if (dto.BranchCode == null) {
//            throw new ValidationException("Отсутствует BranchCode");
//        }
//        if (dto.IsoCurrencyCode == null) {
//            throw new ValidationException("Отсутствует IsoCurrencyCode");
//        }
//        if (dto.urgencyCode == null) {
//            throw new ValidationException("Отсутствует urgencyCode");
//        }
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
//
//    }
//
//    @PostMapping("create")
//    public UserDto create(@RequestBody UserDto user) {
//        return service.create(user);
//    }
}