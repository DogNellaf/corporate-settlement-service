package ru.practice5.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practice5.main.models.TppProduct;

import java.util.List;

public interface TppProductRepository extends JpaRepository<TppProduct, Long> {

    List<TppProduct> findByNumber(String contractNumber);
}