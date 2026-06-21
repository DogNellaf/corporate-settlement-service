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
@Table(name = "tpp_product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TppProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productCodeId;
    private Long clientId;
    private String type;
    private String number;
    private Integer priority;
    private LocalDateTime dateOfConclusion;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Long days;
    private Float penaltyRate;
    private Float nso;
    private Float thresholdAmount;
    private String requisiteType;
    private String interestRateType;
    private Float taxRate;
    @Column(name = "reasone_close")
    private String reasonClose;
    private String state;
}
