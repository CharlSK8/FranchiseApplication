package com.franchises.develop.exception.handler;

public class ProductAlreadyExistsException extends RuntimeException {

    public ProductAlreadyExistsException(String productName) {
        super(String.format("The product '%s' already exists in the branch.", productName));
    }
}
