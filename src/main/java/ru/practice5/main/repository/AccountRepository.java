package ru.practice5.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practice5.main.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}