package com.devrajs.tinydb;

public class TestInput {
    String query;
    ExpectedException expectedException;
    public TestInput(String query, ExpectedException expectedException) {
        this.query = query;
        this.expectedException = expectedException;
    }
}
