package ru.practice5.main.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tpp_product_register")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TppProductRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long product_id;

    @ManyToOne
    @JoinColumn(name = "type")
    private TppRefProductRegisterType type;

    private Long account;
    private String currency_code;
    private String state;
    private String account_number;
}