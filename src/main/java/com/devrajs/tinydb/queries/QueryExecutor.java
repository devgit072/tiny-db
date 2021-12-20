package com.devrajs.tinydb.queries;

import com.devrajs.tinydb.common.Printer;
import com.devrajs.tinydb.common.Util;
import com.devrajs.tinydb.inputs.IInputs;
import com.devrajs.tinydb.inputs.StoredInputs;
import com.devrajs.tinydb.manager.DBMetadata;
import com.devrajs.tinydb.manager.FileManager;
import com.devrajs.tinydb.manager.LogManager;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static com.devrajs.tinydb.manager.StateManager.SLEEP_TIME;

public class QueryExecutor {
    private final IInputs inputs;
    private boolean debugMode = false;

    public QueryExecutor(IInputs inputs) {
        this.inputs = inputs;
    }

    public void init() throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        FileManager.createAllDirectoriesAndFiles();
        DBMetadata.getInstance();
        System.out.println("Welcome to tiny-db.");
        System.out.println("Please login to continue using NND database using this simple syntax: ");
        System.out.println("-u <userName> -p <password>");
    }

    public void executeQueries() throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        while (true) {
            if (SLEEP_TIME != 0) {
                justSleep();
            }
            Util.printCursor();
            String userQuery = inputs.getStringInput();
            if (userQuery.equals("q") || userQuery.equalsIgnoreCase("exit")) {
                System.out.println("Exit from the database. Bye!");
                break;
            }
            try {
                // If inputs is instance of StoredInputs, then print the input in the console.
                if (inputs instanceof StoredInputs) {
                    System.out.println(userQuery);
                }
                processQuery(userQuery);
                LogManager.writeGeneralOrTransactionQueryLog(userQuery);
            } catch (Exception e) {
                Printer.printError(e.getMessage());
                LogManager.writeErrorLog(userQuery, e.getMessage());
                System.out.println("\n");
                System.out.println("Exception for query: " + userQuery);
                if(debugMode) {
                    //e.printStackTrace();
                    throw e;
                }
            }
        }
    }

    void processQuery(String q, IInputs inputs) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        QueryProcessor queryProcessor = new QueryProcessor(q, inputs);
        queryProcessor.parseQuery();
    }

    public void processQuery(String q) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        QueryProcessor queryProcessor = new QueryProcessor(q);
        queryProcessor.parseQuery();
    }

    void justSleep() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setDebugMode(boolean value) {
        debugMode = value;
    }
}
