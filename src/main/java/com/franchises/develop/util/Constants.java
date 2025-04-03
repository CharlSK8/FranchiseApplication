package com.franchises.develop.util;

public final class Constants {

    public static final String MESSAGE_OK = "The request was processed successfully.";
    public static final String MESSAGE_ERROR = "There was an internal error processing the request.";
    public static final String MESSAGE_ERROR_BODY = "Errors were detected in the request body.";
    public static final String MESSAGE_CREATED_FRANCHISE = "The franchise has been successfully created and is now available in the system.";
    public static final String FRANCHISE_ALREADY_EXISTS = "A franchise with this name already exists.";
    public static final String FRANCHISE_NAME_UPDATED = "Franchise name has been successfully updated.";
    public static final String BRANCH_CREATED_SUCCESSFULLY = "Branch has been successfully added.";
    public static final String BRANCH_UPDATED_SUCCESSFULLY = "Branch name has been successfully updated.";
    public static final String BRANCH_NAME_ALREADY_UP_TO_DATE = "Branch name is already up-to-date";
    public static final String PRODUCT_CREATED_SUCCESSFULLY = "Product created successfully.";
    public static final String PRODUCT_REMOVED_SUCCESSFULLY = "Product removed successfully.";
    public static final String PRODUCT_STOCK_UPDATED_SUCCESSFULLY = "Product stock updated successfully.";
    public static final String PRODUCT_NAME_UPDATED_SUCCESSFULLY = "Product name updated successfully.";
    public static final String PRODUCTS_WITH_HIGHEST_STOCK_PER_BRANCH = "Products with highest stock per branch";




    private Constants() {
        throw new IllegalStateException("Constants class");
    }
}