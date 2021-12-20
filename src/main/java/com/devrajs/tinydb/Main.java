package com.devrajs.tinydb;

import com.devrajs.tinydb.inputs.IInputs;
import com.devrajs.tinydb.inputs.UserInput;
import com.devrajs.tinydb.queries.QueryExecutor;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        IInputs inputs = new UserInput();
        QueryExecutor queryExecutor = new QueryExecutor(inputs);
        queryExecutor.init();
        queryExecutor.executeQueries();
    }
}
