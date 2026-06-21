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
@Table(name = "tpp_template_register_balance")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TppTemplateRegisterBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long registerId;
    private Float amount;
    @Column(name = "\"order\"")
    private String order;
    private LocalDateTime lastModifyDate;
}
