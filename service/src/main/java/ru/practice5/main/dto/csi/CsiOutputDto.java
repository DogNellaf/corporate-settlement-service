package ru.practice5.main.dto.csi;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CsiOutputDto {
    private CsiOutputDataParam data;
    private List<String> registerId;
    private List<String> supplementaryAgreementId;
}
