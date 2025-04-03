package com.franchises.develop.exception.handler;

public class BranchNameAlreadyExistsException extends RuntimeException {

    public BranchNameAlreadyExistsException(String branchName) {
        super(String.format("The branch name '%s' already exists.", branchName));
    }
}
