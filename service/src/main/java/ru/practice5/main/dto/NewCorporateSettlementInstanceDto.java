package ru.practice5.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCorporateSettlementInstanceDto {
    private Integer instanceId;
    private String productType;
    private String productCode;
    private String registerType;
    private String mdmCode;
    private String contractNumber;
    private LocalDateTime contractDate;
    private Integer priority;
    private Float interestRatePenalty;
    private Float minimalBalance;
    private Float thresholdAmount;
    private String accountingDetails;
    private String rateType;
    private Float taxPercentageRate;
    private Float technicalOverdraftLimitAmount;
    private Integer contractId;
    private String branchCode;
    private String isoCurrencyCode;
    private String urgencyCode;
    private Integer referenceCode;
    private AdditionalPropertiesVipDto additionalPropertiesVip;
    private List<InstanceArrangementDto> instanceArrangementDto;
}
