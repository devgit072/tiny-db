package com.devrajs.tinydb;

public class ExpectedException {
    boolean exceptionExpected;
    Exception expectedException;
    String expectedErrorMessage;

    public ExpectedException(boolean exceptionExpected, Exception expectedException, String expectedErrorMessage) {
        this.exceptionExpected = exceptionExpected;
        this.expectedException = expectedException;
        this.expectedErrorMessage = expectedErrorMessage;
    }
}
