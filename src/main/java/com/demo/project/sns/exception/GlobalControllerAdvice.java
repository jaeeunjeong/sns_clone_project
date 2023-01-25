package com.demo.project.sns.exception;

import com.demo.project.sns.controller.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 실제 예외를 잡아서 response에 보내주는 것
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(SnsApplicationException.class)
    public ResponseEntity<?> applicationHandler(SnsApplicationException e) {
        log.error("Error {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().name()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> applicationHandler(RuntimeException e) {
        log.error("Error {}", e.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(HttpStatus.INTERNAL_SERVER_ERROR.name()));
    }
}
