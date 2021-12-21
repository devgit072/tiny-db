package com.devrajs.tinydb.exception;

public class QuerySyntaxException extends RuntimeException {
    public QuerySyntaxException() {
        super();
    }

    public QuerySyntaxException(String errorMessage) {
        super(errorMessage);
    }

    public QuerySyntaxException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
