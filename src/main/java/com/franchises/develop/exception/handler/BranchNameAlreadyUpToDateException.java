package com.franchises.develop.exception.handler;

public class BranchNameAlreadyUpToDateException extends RuntimeException {
    public BranchNameAlreadyUpToDateException(String branchId, String name) {
        super("Branch with ID " + branchId + " already has the name: " + name);
    }
}
