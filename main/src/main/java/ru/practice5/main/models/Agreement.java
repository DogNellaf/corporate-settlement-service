package ru.practice5.main.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne
    @JoinColumn(name = "product_id")
    private TppProduct product;

    private String general_agreement_id;
    private String supplementary_agreement_id;
    private String arrangement_type;
    private Long sheduler_job_id;
    private String number;
    private LocalDateTime opening_date;
    private LocalDateTime closing_date;
    private LocalDateTime cancel_date;
    private Long validity_duration;
    private String cancellation_reason;
    private String status;
    private LocalDateTime interest_calculation_date;
    private Float interest_rate;
    private Float coefficient;
    private String coefficient_action;
    private Float minimum_interest_rate;
    private Float minimum_interest_rate_coefficient;
    private String minimum_interest_rate_coefficient_action;
    private Float maximal_interest_rate;
    private Float maximal_interest_rate_coefficient;
    private String maximal_interest_rate_coefficient_action;
}