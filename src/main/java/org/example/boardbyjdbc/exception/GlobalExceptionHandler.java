package org.example.boardbyjdbc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<String> handleBoardNotFound(BoardNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("404 에러: " + ex.getMessage());
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<String> handlePasswordMismatch(PasswordMismatchException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("인증 에러: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 에러가 발생했습니다. 관리자에게 문의하세요.");
    }
}

