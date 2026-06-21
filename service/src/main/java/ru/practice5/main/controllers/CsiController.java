package ru.practice5.main.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practice5.main.dto.NewCorporateSettlementInstanceDto;
import ru.practice5.main.dto.csi.CsiOutputDto;
import ru.practice5.main.services.CsiService;

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
