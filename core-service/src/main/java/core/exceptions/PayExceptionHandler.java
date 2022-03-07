package core.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import web.exception.AppError;
import web.exception.PayException;

@ControllerAdvice
@Slf4j
public class PayExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<AppError> payStatusNotSupportedException(PayException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.name(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
