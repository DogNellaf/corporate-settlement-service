package ru.practice5.main.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account_pool")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountPool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String account_pool;
    private String currency_code;
    private String mdm_code;
    private String priority_code;
    private String registry_type_code;
}