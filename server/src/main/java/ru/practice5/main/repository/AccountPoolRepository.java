package ru.practice5.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practice5.main.models.AccountPool;

public interface AccountPoolRepository extends JpaRepository<AccountPool, Long> {

}