package com.devrajs.tinydb.manager;

import com.devrajs.tinydb.common.FileHelper;
import com.devrajs.tinydb.model.Database;
import com.devrajs.tinydb.model.Table;
import com.devrajs.tinydb.model.User;
import com.devrajs.tinydb.queries.QueryProcessor;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DumpManager {
    public void dumpDatabase(String databaseName, boolean withData) throws IOException, ClassNotFoundException {
        User user = StateManager.getCurrentUser();
        Database database = user.getDatabase(databaseName);
        List<String> dumpQueries = getDumpQueries(database);
        if (withData) {
            getTableContentQueries();
        }

        String filePath = String.format("SQLDump/%s.sql", databaseName);
        FileHelper.writeIntoFile(filePath, dumpQueries);
        System.out.println("SQL dump has been created in file: " + filePath);
    }

    private List<String> getDumpQueries(Database database) {
        return addTables(database);
    }

    private String addDatabase(String databaseName) {
        return String.format("CREATE DATABASE %s;", databaseName);
    }

    private String useDatabase(String databaseName) {
        return String.format("USE %s;", databaseName);
    }

    private List<String> addTables(Database database) {
        List<Table> tableList = database.getTableList();
        List<String> queryList = new ArrayList<>();
        for (Table table : tableList) {
            String tableQuery = addTable(table);
            queryList.add(tableQuery);
            queryList.add(System.lineSeparator());
        }
        return queryList;
    }

    private String addTable(Table table) {
        StringBuilder tableQuery = new StringBuilder("CREATE TABLE");
        tableQuery.append(" ");
        tableQuery.append(table.getTableName());
        Map<String, String> columnAndItsTypes = table.getColumnAndItsTypes();
        tableQuery.append("(");
        boolean secondOrLaterColumn = false;
        for (Map.Entry<String, String> entry : columnAndItsTypes.entrySet()) {
            if (secondOrLaterColumn) {
                tableQuery.append(", ");
            }
            String columnName = entry.getKey();
            String columnType = entry.getValue();
            tableQuery.append(String.format("%s %s", columnName, columnType));
            secondOrLaterColumn = true;
        }
        tableQuery.append(");");
        return tableQuery.toString();
    }

    private String getTableContentQueries() {
        return "Not implemented now";
    }

    public void sourceDump(String fileName, String databaseName) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        List<String> inputs = new ArrayList<>();
        inputs.add(String.format("CREATE DATABASE %s;", databaseName));
        inputs.add(String.format("USE %s;", databaseName));
        inputs.addAll(FileHelper.readFromFile(fileName));

        for (String input : inputs) {
            if (input.trim().isEmpty()) {
                continue;
            }
            QueryProcessor queryProcessor = new QueryProcessor(input);
            queryProcessor.parseQuery();
        }
    }
}