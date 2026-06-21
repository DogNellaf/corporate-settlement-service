package ru.practice5.main.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "agreement")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private String generalAgreementId;
    private String supplementaryAgreementId;
    private String arrangementType;
    @Column(name = "sheduler_job_id")
    private Long schedulerJobId;
    private String number;
    private LocalDateTime openingDate;
    private LocalDateTime closingDate;
    private LocalDateTime cancelDate;
    private Long validityDuration;
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
