package com.devrajs.tinydb;

public class ExpectedException {
    Exception expectedException;
    String expectedErrorMessage;

    public ExpectedException(Exception expectedException, String expectedErrorMessage) {
        this.expectedException = expectedException;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    public Exception getExpectedException() {
        return expectedException;
    }

    public String getExpectedErrorMessage() {
        return expectedErrorMessage;
    }

}
