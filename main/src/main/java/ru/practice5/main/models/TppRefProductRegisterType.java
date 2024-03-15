package ru.practice5.main.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tpp_ref_product_register_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TppRefProductRegisterType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internal_id;
    private String value;
    private String register_type_name;

    @ManyToOne
    @JoinColumn(name = "product_class_code")
    private TppRefProductClass product_class;

    private LocalDateTime register_type_start_date;
    private LocalDateTime register_type_end_date;

    @ManyToOne
    @JoinColumn(name = "account_type")
    private TppRefAccountType account_type;
}