package ru.practice5.main.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParamDto {
    public String key;
    public String value;
    public String name;
}
