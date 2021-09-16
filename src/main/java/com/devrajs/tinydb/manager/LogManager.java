package com.devrajs.tinydb.manager;

import java.io.IOException;

import static com.devrajs.tinydb.common.FileHelper.writeIntofile;
import static com.devrajs.tinydb.manager.FileConstants.*;

public class LogManager {

    public static void writeErrorLog(String query, String errMsg) throws IOException {
        String lineToBeWritten = String.format("Query: %s , Error: %s\n", query, errMsg);
        writeIntofile(ERROR_LOG, lineToBeWritten);
    }

    public static void writeGeneralOrTrasactionQueryLog(String query) throws IOException {
        String lineToBeWritten = String.format("Query: %s , Status: SUCCESS\n", query);
        writeIntofile(GENERAL_QUERY_LOG, lineToBeWritten);
        if (StateManager.is_transaction_on) {
            writeTrasactionLog(query);
        } else if (query.contains("transaction")) {
            writeTrasactionLog(query);
        }
    }

    public static void writeGeneralQueryLog(String query) throws IOException {
        String lineToBeWritten = String.format("Query: %s , Status: SUCCESS", query);
        writeIntofile(GENERAL_QUERY_LOG, lineToBeWritten);
    }

    public static void writeTrasactionLog(String query) throws IOException {
        String lineToBeWritten = String.format("Query: %s , Status: SUCCESS\n", query);
        writeIntofile(TRANSACTION_LOG, lineToBeWritten);
    }
}
