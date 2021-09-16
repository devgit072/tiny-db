package com.devrajs.tinydb;

import com.devrajs.tinydb.inputs.IInputs;
import com.devrajs.tinydb.inputs.UserInput;
import com.devrajs.tinydb.queries.QueryExecutor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        IInputs inputs = new UserInput();
        QueryExecutor queryExecutor = new QueryExecutor(inputs);
        queryExecutor.startApplication();
    }
}
