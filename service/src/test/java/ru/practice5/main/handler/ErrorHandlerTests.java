package ru.practice5.main.handler;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.dao.DataIntegrityViolationException;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerTests {
    @ParameterizedTest
    @ValueSource(strings={"uq_product_number","uq_register_product_type","PUBLIC.UQ_AGREEMENT_NUMBER_INDEX_8"})
    void knownBusinessConflictsAre400(String name) {
        var cause=new ConstraintViolationException("private database detail",new SQLException("private SQL","23505"),name);
        var result=new ErrorHandler().handleIntegrityConflict(new DataIntegrityViolationException("insert SQL",cause));
        assertEquals(400,result.getStatusCodeValue());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().getError().contains("уже существует"));
        assertFalse(result.getBody().getError().contains("private"));
    }

    @Test
    void unrelatedIntegrityFailureIsNotReportedAsDuplicate() {
        var cause=new ConstraintViolationException("private database detail",new SQLException("private SQL","23503"),"fk_other");
        var result=new ErrorHandler().handleIntegrityConflict(new DataIntegrityViolationException("insert SQL",cause));
        assertEquals(500,result.getStatusCodeValue());
        assertEquals("Не удалось сохранить данные.",result.getBody().getError());
    }
}
