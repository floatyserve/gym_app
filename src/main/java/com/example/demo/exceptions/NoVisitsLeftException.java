package com.example.demo.exceptions;

public class NoVisitsLeftException extends RuntimeException {
    public NoVisitsLeftException(String message) {
        super(message);
    }
}
