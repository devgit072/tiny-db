package com.devrajs.tinydb.exception;

public class UnexpectedRuntimeException extends RuntimeException {
    public UnexpectedRuntimeException() {
    }

    public UnexpectedRuntimeException(String errorMessage) {
        super(errorMessage);
    }

    public UnexpectedRuntimeException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
