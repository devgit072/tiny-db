package com.devrajs.tinydb.exception;

public class QueryErrorException extends RuntimeException {
    public QueryErrorException() {
        super();
    }

    public QueryErrorException(String errorMessage) {
        super(errorMessage);
    }

    public QueryErrorException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}