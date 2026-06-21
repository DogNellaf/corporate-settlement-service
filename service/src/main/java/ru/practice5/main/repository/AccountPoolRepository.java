package ru.practice5.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practice5.main.models.AccountPool;

import java.util.List;

public interface AccountPoolRepository extends JpaRepository<AccountPool, Long> {

    @Query(value = "SELECT * FROM account_pool WHERE "
            + "(:branchCode IS NULL OR branch_code = :branchCode) AND "
            + "(:currencyCode IS NULL OR currency_code = :currencyCode) AND "
            + "(:mdmCode IS NULL OR mdm_code = :mdmCode) AND "
            + "(:priorityCode IS NULL OR priority_code = :priorityCode) AND "
            + "(:registryTypeCode IS NULL OR registry_type_code = :registryTypeCode)",
            nativeQuery = true)
    List<AccountPool> findByParams(
            @Param("branchCode") String branchCode,
            @Param("currencyCode") String currencyCode,
            @Param("mdmCode") String mdmCode,
            @Param("priorityCode") String priorityCode,
            @Param("registryTypeCode") String registryTypeCode
    );
}
