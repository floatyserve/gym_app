package com.example.demo.exceptions;

public class NoActiveMembershipException extends RuntimeException {
    public NoActiveMembershipException(String message) {
        super(message);
    }
}
