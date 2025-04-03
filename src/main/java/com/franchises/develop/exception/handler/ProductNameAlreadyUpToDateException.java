package com.franchises.develop.exception.handler;

public class ProductNameAlreadyUpToDateException extends RuntimeException {
    public ProductNameAlreadyUpToDateException(String productId, String name) {
        super("Product with ID " + productId + " already has the name: " + name);
    }
}

