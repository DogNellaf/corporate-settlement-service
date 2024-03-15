package ru.practice5.main.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private Long register_id;
    private Float amount;
    private String order;
    private LocalDateTime last_modify_date;
}