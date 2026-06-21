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

@Entity
@Table(name = "tpp_ref_product_class")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TppRefProductClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internalId;
    private String value;
    private String gbiCode;
    private String gbiName;
    private String productRowCode;
    private String productRowName;
    private String subclassCode;
    private String subclassName;
}
