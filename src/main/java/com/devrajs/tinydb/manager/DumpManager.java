package com.devrajs.tinydb.manager;

import com.devrajs.tinydb.common.FileHelper;
import com.devrajs.tinydb.model.Database;
import com.devrajs.tinydb.model.Table;
import com.devrajs.tinydb.model.TableContent;
import com.devrajs.tinydb.model.User;
import com.devrajs.tinydb.queries.QueryProcessor;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DumpManager {
    public void dumpDatabase(String databaseName, boolean withData, String filePath) throws IOException, ClassNotFoundException {
        User user = StateManager.getCurrentUser();
        Database database = user.getDatabase(databaseName);
        List<String> tableMetadataQueries = addTables(database);
        List<String> dumpQueries = new ArrayList<>(tableMetadataQueries);
        if (withData) {
            List<String> tableContentQueries = getTablesContentQueries(database);
            dumpQueries.addAll(tableContentQueries);
        }
        if (filePath.isEmpty()) {
            filePath = String.format("SQLDump/%s.sql", databaseName);
        }
        FileHelper.writeIntoFile(filePath, dumpQueries);
        System.out.println("SQL dump has been created in file: " + filePath);
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
        List<String> primaryKeys = table.getPrimaryKeyColumns();
        List<String> foreignKeys = table.getForeignKeysForeignTableAndColumn();
        tableQuery.append("(");
        boolean secondOrLaterColumn = false;
        for (Map.Entry<String, String> entry : columnAndItsTypes.entrySet()) {
            if (secondOrLaterColumn) {
                tableQuery.append(", ");
            }
            String columnName = entry.getKey();
            String columnType = entry.getValue();
            String pkConstraints = "";
            if (primaryKeys.contains(columnName)) {
                pkConstraints = "primary key";
            }
            String fkConstraints = "";
            if (foreignKeys.contains(columnName)) {
                int i = foreignKeys.indexOf(columnName);
                String foreignTable = foreignKeys.get(i + 1);
                String foreignColumn = foreignKeys.get(i + 2);
                fkConstraints = String.format("references %s(%s)", foreignTable, foreignColumn);
            }
            tableQuery.append(String.format("%s %s %s %s", columnName, columnType, pkConstraints, fkConstraints));
            secondOrLaterColumn = true;
        }
        tableQuery.append(");");
        return tableQuery.toString();
    }

    private List<String> getTablesContentQueries(Database database) throws IOException, ClassNotFoundException {
        List<Table> tableList = database.getTableList();
        List<String> queryList = new ArrayList<>();
        for (Table table : tableList) {
            List<String> tableQueries = getTableContentQuery(table);
            if (tableQueries.isEmpty()) {
                continue;
            }
            queryList.addAll(tableQueries);
        }
        return queryList;
    }

    private List<String> getTableContentQuery(Table table) throws IOException, ClassNotFoundException {
        Map<String, String> columnAndItsTypes = table.getColumnAndItsTypes();
        DBContents dbContents = DBContents.getInstance();
        TableContent tableContent = dbContents.getTableContent(table.getTableId());
        if (tableContent == null) {
            return Collections.emptyList();
        } else if (tableContent.getRows().isEmpty()) {
            return Collections.emptyList();
        }
        String columnsStr = "";
        boolean moreThanOneColumn = false;
        for (Map.Entry<String, String> type : columnAndItsTypes.entrySet()) {
            if (moreThanOneColumn) {
                columnsStr = String.format("%s, %s", columnsStr, type.getKey());
            } else {
                columnsStr = type.getKey();
            }
            moreThanOneColumn = true;
        }

        List<String> rowQueries = new ArrayList<>();
        List<Map<String, Object>> rows = tableContent.getRows();
        for (Map<String, Object> row : rows) {
            moreThanOneColumn = false;
            String columnValues = "";
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String columnType = columnAndItsTypes.get(entry.getKey());
                String columnValue = entry.getValue().toString();
                if (moreThanOneColumn) {
                    columnValues = String.format("%s, %s", columnValues, columnValue);
                } else {
                    columnValues = columnValue;
                }
                moreThanOneColumn = true;
            }
            String rowQuery = String.format(
                    "insert into %s values(%s);", table.getTableName(), columnValues
            );
            rowQueries.add(rowQuery);
            rowQueries.add(System.lineSeparator());
        }
        return rowQueries;
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