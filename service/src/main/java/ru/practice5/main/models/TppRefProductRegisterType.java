package ru.practice5.main.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internalId;
    private String value;
    private String registerTypeName;
    private String productClassCode;
    private LocalDateTime registerTypeStartDate;
    private LocalDateTime registerTypeEndDate;
    private String accountType;
}
