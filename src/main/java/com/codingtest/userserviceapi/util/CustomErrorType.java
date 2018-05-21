package com.codingtest.userserviceapi.util;

/*
Utility class to Return Custom Error Messages
 */
public class CustomErrorType {
    private String errorMessage;

    public CustomErrorType(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
