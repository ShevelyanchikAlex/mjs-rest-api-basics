package com.epam.esm.exception;

public class ServiceException extends RuntimeException {
    private final String errorCode;
    private final Object[] args;

    public ServiceException(String errorCode, Object... args) {
        this.errorCode = errorCode;
        this.args = args;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Object[] getArgs() {
        return args;
    }
}