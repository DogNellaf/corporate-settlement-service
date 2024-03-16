package ru.practice5.main.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "tpp_ref_product_register_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TppRefProductRegisterType {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internal_id;
    private String value;
    private String register_type_name;
    private String product_class_code;
    private LocalDateTime register_type_start_date;
    private LocalDateTime register_type_end_date;
    private String account_type;
}