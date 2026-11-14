package ru.practice5.main.handler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practice5.main.exception.NotFoundException;
import ru.practice5.main.exception.ValidationException;
import java.util.Locale;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerNotFoundException(NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIntegrityConflict(DataIntegrityViolationException e) {
        // The transaction interceptor has already rolled back the failed operation.
        // Only known business-key conflicts are reported as duplicate requests.
        for (Throwable cause = e; cause != null; cause = cause.getCause()) {
            if (cause instanceof ConstraintViolationException) {
                String name = ((ConstraintViolationException) cause).getConstraintName();
                name = name == null ? "" : name.toLowerCase(Locale.ROOT);
                String message = name.contains("uq_product_number") ? "Договор с таким номером уже существует." :
                        name.contains("uq_register_product_type") ? "Регистр этого типа уже существует для продукта." :
                        name.contains("uq_agreement_number") ? "Дополнительное соглашение с таким номером уже существует." : null;
                if (message != null) {
                    return ResponseEntity.badRequest().body(new ErrorResponse(message));
                }
            }
        }
        log.error("Unexpected database integrity failure", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Не удалось сохранить данные."));
    }

}

