package org.example.boardbyjdbc.exception;


public class BoardNotFoundException extends RuntimeException {

    public BoardNotFoundException(String message) {
        super(message);
    }
}