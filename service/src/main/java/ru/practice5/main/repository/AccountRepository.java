package ru.practice5.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practice5.main.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT * " +
            "FROM account a " +
            "WHERE a.account_pool_id = :id " +
            "AND (a.bussy = false OR a.bussy IS NULL) " +
            "AND NOT EXISTS (SELECT 1 FROM tpp_product_register r WHERE r.account = a.id) " +
            "ORDER BY a.id LIMIT 1 FOR UPDATE",
            nativeQuery = true)
    Account findFreeForUpdateByAccountPoolId(Long id);
}
