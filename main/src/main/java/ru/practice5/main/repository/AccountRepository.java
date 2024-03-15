package ru.practice5.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practice5.main.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT * " +
            "FROM account a " +
            "WHERE a.account_pool_id = :id " +
            "LIMIT 1",
            nativeQuery = true)
    Account findOneByAccountPoolId(Long id);
}