package ru.practice5.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practice5.main.models.TppProductRegister;

import java.util.List;

public interface TppProductRegisterRepository extends JpaRepository<TppProductRegister, Long> {
    @Query(value = "SELECT * " +
            "FROM tpp_product_register r " +
            "WHERE r.product_id = :productId AND r.type = :type",
            nativeQuery = true)
    List<TppProductRegister> findAllByProductIdAndType(Long productId, String type);
}