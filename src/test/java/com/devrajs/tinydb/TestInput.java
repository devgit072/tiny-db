package com.devrajs.tinydb;

public class TestInput {
    String query;
    boolean exceptionExpected;
    String expectedErrorMessage;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isExceptionExpected() {
        return exceptionExpected;
    }

    public void setExceptionExpected(boolean exceptionExpected) {
        this.exceptionExpected = exceptionExpected;
    }

    public String getExpectedErrorMessage() {
        return expectedErrorMessage;
    }

    public void setExpectedErrorMessage(String expectedErrorMessage) {
        this.expectedErrorMessage = expectedErrorMessage;
    }
}
