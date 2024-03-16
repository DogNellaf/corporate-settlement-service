package ru.practice5.main.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tpp_ref_product_class")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TppRefProductClass {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internal_id;
    private String value;
    private String gbi_code;
    private String gbi_name;
    private String product_row_code;
    private String product_row_name;
    private String subclass_code;
    private String subclass_name;
}