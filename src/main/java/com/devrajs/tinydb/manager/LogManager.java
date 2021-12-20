package com.devrajs.tinydb.manager;

import com.devrajs.tinydb.common.FileHelper;

import java.io.IOException;

import static com.devrajs.tinydb.common.FileHelper.writeIntoFile;
import static com.devrajs.tinydb.manager.FileConstants.*;

public class LogManager {

    public static void writeErrorLog(String query, String errMsg) throws IOException {
        String lineToBeWritten = String.format("Query: %s , Error: %s\n", query, errMsg);
        FileHelper.writeIntoFile(ERROR_LOG, lineToBeWritten);
    }

    public static void writeGeneralOrTransactionQueryLog(String query) throws IOException {
        String lineToBeWritten = String.format("Query: %s , Status: SUCCESS\n", query);
        FileHelper.writeIntoFile(GENERAL_QUERY_LOG, lineToBeWritten);
        if (StateManager.is_transaction_on) {
            writeTransactionLog(query);
        } else if (query.contains("transaction")) {
            writeTransactionLog(query);
        }
    }

    public static void writeGeneralQueryLog(String query) throws IOException {
        String lineToBeWritten = String.format("Query: %s , Status: SUCCESS", query);
        FileHelper.writeIntoFile(GENERAL_QUERY_LOG, lineToBeWritten);
    }

    public static void writeTransactionLog(String query) throws IOException {
        String lineToBeWritten = String.format("Query: %s , Status: SUCCESS\n", query);
        FileHelper.writeIntoFile(TRANSACTION_LOG, lineToBeWritten);
    }
}
