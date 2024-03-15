package ru.practice5.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practice5.main.models.TppProductRegister;

import java.util.List;

public interface TppProductRegisterRepository extends JpaRepository<TppProductRegister, Long> {
    List<TppProductRegister> findAllByProductIdAndType(Long productId, String type);
}