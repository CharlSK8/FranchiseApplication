package com.franchises.develop.exception.handler;

public class BranchNotFoundException extends RuntimeException {

    public BranchNotFoundException(String id) {
        super("No branch found with id " + id +". Please verify the ID and try again.");
    }

    public BranchNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}