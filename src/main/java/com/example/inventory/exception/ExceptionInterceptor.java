package com.example.inventory.exception;

import jakarta.transaction.TransactionalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(value
//            = { TransactionalException.class, Exception.class })
//    protected ResponseEntity<Object> handleTransactionalError(
//            RuntimeException ex, WebRequest request) {
//        return ResponseEntity.internalServerError().body("Transactional exception, changes not commited");
//    }

    @ExceptionHandler(value
            = { Exception.class })
    protected ResponseEntity<Object> handleUncaughtError(
            RuntimeException ex, WebRequest request) {
        ex.printStackTrace();
        log.error("Error occurred : {}",ex.toString());
        return ResponseEntity.internalServerError().body("Internal server error");
    }
}
