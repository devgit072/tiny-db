package com.devrajs.tinydb.manager;

public class DumpManager {
    public static final String SQL_DUMP = "SQLDump";
    private String databaseName = "";
    private String dumpSQLQueries = "";

    public DumpManager(String databaseName) {
        this.databaseName = databaseName;
    }

    public void initDBQuery() {

    }


    public void dumpDatabase(String databaseName, boolean withData) {
        System.out.println("Not implemented.");
    }

    public void sourceDump(String fileName) {
        System.out.println("Not implemented.");
    }
}