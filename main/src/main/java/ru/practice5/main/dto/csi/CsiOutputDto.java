package ru.practice5.main.dto.csi;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class CsiOutputDto {
    public CsiOutputDataParam data;
    public List<String> registerId;
    public List<String> supplementaryAgreementId;
}
