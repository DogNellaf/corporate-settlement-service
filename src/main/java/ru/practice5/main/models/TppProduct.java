package ru.practice5.main.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Long product_code_id;
    private Long client_id;
    private String type;
    private String number;
    private Long priority;
    private LocalDateTime date_of_conclusion;
    private LocalDateTime start_date_time;
    private LocalDateTime end_date_time;
    private Long days;
    private Float penalty_rate;
    private Float nso;
    private Float threshold_amount;
    private String requisite_type;
    private String interest_rate_type;
    private Float tax_rate;
    private String reasone_close;
    private String state;
}