package ru.practice5.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCorporateSettlementAccountDto {
    public Long instanceId;
    public String registryTypeCode;
    public String accountType;
    public String currencyCode;
    public String branchCode;
    public String priorityCode;
    public String mdmCode;
    public String clientCode;
    public String trainRegion;
    public String counter;
    public String salesCode;
}
