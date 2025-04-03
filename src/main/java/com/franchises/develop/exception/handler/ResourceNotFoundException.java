package com.franchises.develop.exception.handler;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String id) {
        super("No franchise found with id " + id +". Please verify the ID and try again.");
    }
}
