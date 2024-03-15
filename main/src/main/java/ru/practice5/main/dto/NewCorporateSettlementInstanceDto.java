package ru.practice5.main.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewCorporateSettlementInstanceDto {
    public Integer instanceId;
    public String productType;
    public String productCode;
    public String registerType;
    public String mdmCode;
    public String contractNumber;
    public LocalDateTime contractDate;
    public Integer priority;
    public Float interestRatePenalty;
    public Float minimalBalance;
    public Float thresholdAmount;
    public String accountingDetails;
    public String rateType;
    public Float taxPercentageRate;
    public Float technicalOverdraftLimitAmount;
    public Integer contractId;
    public String BranchCode;
    public String IsoCurrencyCode;
    public String urgencyCode;
    public Integer ReferenceCode;
    public AdditionalPropertiesVipDto additionalPropertiesVip;
    public List<InstanceArrangementDto> InstanceArrangementDto;
}
