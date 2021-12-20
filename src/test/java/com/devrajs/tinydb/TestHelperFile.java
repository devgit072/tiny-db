package com.devrajs.tinydb;

import java.io.File;

public class TestHelperFile {

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
