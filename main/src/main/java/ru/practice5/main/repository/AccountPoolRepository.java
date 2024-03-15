package ru.practice5.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practice5.main.models.AccountPool;

import java.util.List;

public interface AccountPoolRepository extends JpaRepository<AccountPool, Long> {

}