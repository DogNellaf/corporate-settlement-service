package ru.practice5.main.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practice5.main.services.CsiService;
import ru.practice5.main.dto.CsiOutputDto;
import ru.practice5.main.dto.NewCorporateSettlementInstanceDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/corporate-settlement-instance")
public class CsiController {

    private final CsiService service;

    @PostMapping("create")
    public CsiOutputDto create(@RequestBody NewCorporateSettlementInstanceDto dto) {
        return service.create(dto);
    }
}