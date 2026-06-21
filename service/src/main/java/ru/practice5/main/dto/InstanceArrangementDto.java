package ru.practice5.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstanceArrangementDto {
    private String generalAgreementId;
    private String supplementaryAgreementId;
    private String arrangementType;
    private Integer schedulerJobId;
    private String number;
    private LocalDateTime openingDate;
    private LocalDateTime closingDate;
    private LocalDateTime cancelDate;
    private Integer validityDuration;
    private String cancellationReason;
    private String status;
    private LocalDateTime interestCalculationDate;
    private Float interestRate;
    private Float coefficient;
    private String coefficientAction;
    private Float minimumInterestRate;
    private Float minimumInterestRateCoefficient;
    private String minimumInterestRateCoefficientAction;
    private Float maximalInterestRate;
    private Float maximalInterestRateCoefficient;
    private String maximalInterestRateCoefficientAction;
}
