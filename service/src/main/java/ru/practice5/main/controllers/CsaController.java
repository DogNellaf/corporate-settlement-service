package ru.practice5.main.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practice5.main.dto.NewCorporateSettlementAccountDto;
import ru.practice5.main.dto.csa.CsaOutputDto;
import ru.practice5.main.services.CsaService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/corporate-settlement-account")
public class CsaController {

    private final CsaService service;

    @PostMapping("create")
    public CsaOutputDto create(@RequestBody NewCorporateSettlementAccountDto dto) {
        return service.create(dto);
    }
}
