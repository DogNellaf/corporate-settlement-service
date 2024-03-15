package ru.practice5.main;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practice5.main.dto.CsaOutputDto;
import ru.practice5.main.dto.NewCorporateSettlementAccountDto;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/corporate-settlement-account")
@Transactional
public class CsaController {

    private final CsaService service;

    @PostMapping("create")
    public CsaOutputDto create(@RequestBody NewCorporateSettlementAccountDto dto) {
        return service.create(dto);
    }
}
