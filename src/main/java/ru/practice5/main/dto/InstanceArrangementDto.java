package ru.practice5.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class InstanceArrangementDto {
    public String GeneralAgreementId;
    public String SupplementaryAgreementId;
    public String arrangementType;
    public Integer shedulerJobId;
    public String Number;
    public LocalDateTime openingDate;
    public LocalDateTime closingDate;
    public LocalDateTime CancelDate;
    public Integer validityDuration;
    public String cancellationReason;
    public String Status;
    public LocalDateTime interestCalculationDate;
    public Float interestRate;
    public Float coefficient;
    public String coefficientAction;
    public Float minimumInterestRate;
    public String minimumInterestRateCoefficient;
    public String minimumInterestRateCoefficientAction;
    public Float maximalnterestRate;
    public Float maximalnterestRateCoefficient;
    public String maximalnterestRateCoefficientAction;
}
