package ru.practice5.main.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Verify schema constraints independently of the service's preliminary SELECTs. */
@SpringBootTest
@Transactional
class BusinessKeyConstraintTests {
    @Autowired JdbcTemplate jdbc;

    @Test void contractNumberIsUnique() {
        assertThrows(DataIntegrityViolationException.class,()->jdbc.update(
                "INSERT INTO tpp_product(id,number) VALUES(9900,'P001')"));
    }
    @Test void productRegisterTypeIsUnique() {
        jdbc.update("INSERT INTO tpp_product_register(id,product_id,type,account) VALUES(9900,1,'03.012.002_47533_ComSoLd',1)");
        assertThrows(DataIntegrityViolationException.class,()->jdbc.update(
                "INSERT INTO tpp_product_register(id,product_id,type,account) VALUES(9901,1,'03.012.002_47533_ComSoLd',2)"));
    }
    @Test void agreementNumberIsUnique() {
        assertThrows(DataIntegrityViolationException.class,()->jdbc.update(
                "INSERT INTO agreement(id,product_id,number) VALUES(9900,1,'Agreement001')"));
    }
}
