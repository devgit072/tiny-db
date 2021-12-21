package com.devrajs.tinydb;

import com.devrajs.tinydb.inputs.StoredInputs;
import com.devrajs.tinydb.queries.QueryExecutor;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class TestHelperFile {

    /**
     * Create a QueryExecutor instance and initiate it. Created this method because
     * many test methods same code repeatedly.
     */
    public static QueryExecutor getQueryExecutor(StoredInputs inputs) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        QueryExecutor executor = new QueryExecutor(inputs);
        executor.init();
        executor.setDebugMode(true);
        return executor;
    }

    String getMostRecentFile() {
        File sqlDumpDir = new File("SQLDump");
        if(!sqlDumpDir.exists()) {
            throw new RuntimeException("SQL dump folder doesn exists");
        }
        File[] files = sqlDumpDir.listFiles();
        if (files == null) {
            throw new RuntimeException("No file exists");
        }
        long recentTime = Long.MIN_VALUE;
        String mostRecentFile = "";
        for (File file : files) {
            long lastModifiedTime = file.lastModified();
            if (lastModifiedTime > recentTime) {
                mostRecentFile = file.getAbsolutePath();
            }
        }
        return mostRecentFile;
    }
}
