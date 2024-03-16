package ru.practice5.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practice5.main.models.Agreement;

import java.util.List;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    List<Agreement> findByNumber(String number);
    List<Agreement> findAllByNumberIn(List<String> numbers);
}