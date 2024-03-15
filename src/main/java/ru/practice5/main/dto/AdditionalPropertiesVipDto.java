package ru.practice5.main.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdditionalPropertiesVipDto {
    public List<ParamDto> data;
}
