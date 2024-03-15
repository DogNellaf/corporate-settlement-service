package ru.practice5.main.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tpp_ref_account_type") //TODO
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TppRefAccountType {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internal_id;
    private String value;
}