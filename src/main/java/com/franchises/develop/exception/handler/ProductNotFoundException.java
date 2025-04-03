package com.franchises.develop.exception.handler;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String id) {
        super("No product found with id " + id +". Please verify the ID and try again.");
    }
}
