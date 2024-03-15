package ru.practice5.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practice5.main.models.TppRefProductRegisterType;

import java.util.List;

public interface TppRefProductRegisterTypeRepository extends JpaRepository<TppRefProductRegisterType, Long> {
    @Query(value = "SELECT * " +
            "FROM tpp_ref_product_register_type rt " +
            "JOIN tpp_ref_product_class cl ON rt.product_class_code == cl.value" +
            "WHERE cl.value == :productCode AND rt.account_type == 'Клиентский'",
            nativeQuery = true)
    List<TppRefProductRegisterType> findByValue(String productCode);
}
