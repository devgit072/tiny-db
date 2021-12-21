package com.devrajs.tinydb.manager;

import com.devrajs.tinydb.common.Condition;
import com.devrajs.tinydb.common.Operator;
import com.devrajs.tinydb.common.Printer;
import com.devrajs.tinydb.common.SIGN;
import com.devrajs.tinydb.exception.QueryErrorException;
import com.devrajs.tinydb.exception.QuerySyntaxException;
import com.devrajs.tinydb.model.Database;
import com.devrajs.tinydb.model.Table;
import com.devrajs.tinydb.model.TableContent;

import java.io.*;
import java.rmi.RemoteException;
import java.util.*;

public class DBContents {
    private static Map<String, TableContent> tableIdToTableContentMap;
    private static final String contentFile = FileConstants.CONTENT_FILE;
    private static DBContents dbContents;

    public static DBContents getInstance() throws IOException, ClassNotFoundException {
        if (dbContents == null) {
            dbContents = new DBContents();
        }
        return dbContents;
    }

    private DBContents() throws IOException, ClassNotFoundException {
        init();
    }

    public static void init() throws IOException, ClassNotFoundException {
        updateRam();
    }

    public static void insertRowInTable(String tableName, List<String> columnValues)
            throws IOException, ClassNotFoundException {
        Database database = StateManager.getCurrentDB();
        Table table = database.getTable(tableName);
        if (table == null) {
            throw new QueryErrorException(String.format("Table: %s doesn't exist under the database: %s", tableName,
                    database.getDatabaseName()));
        }
        checkColumnContentAndColumnType(table.getColumnTypes(), columnValues);
        String tableId = table.getTableId();
        List<String> columns = getColumns(table.getColumnAndItsTypes());
        Map<String, Object> row = prepareRow(columnValues, columns);
        updateRam();
        checkForeignKeyViolation(table, row);
        if (LockManager.isTableLocked(tableId)) {
            throw new QueryErrorException(String.format("Table: %s is locked due to other ongoing transaction", table.getTableName()));
        }
        LockManager.lockTableIfTransactionIsOn(tableId);
        TableContent tableContent;
        if (tableIdToTableContentMap.containsKey(tableId)) {
            tableContent = tableIdToTableContentMap.get(tableId);
        } else {
            tableContent = new TableContent(tableId, table.getColumnAndItsTypes());
        }
        doesItViolatePrimaryKeyConstraints(table, tableContent, row);
        tableContent.addRow(row);
        tableIdToTableContentMap.put(tableId, tableContent);
        updateDisk();
    }

    private static void doesItViolatePrimaryKeyConstraints(Table table, TableContent existingContent,
                                                           Map<String, Object> newRow) {
        List<String> primaryKeys = table.getPrimaryKeyColumns();
        List<Map<String, Object>> rows = existingContent.getRows();
        for (Map<String, Object> r : rows) {
            for (String pk : primaryKeys) {
                String oldValue = r.get(pk).toString();
                String newValue = newRow.get(pk).toString();
                if (oldValue.equals(newValue)) {
                    throw new QueryErrorException(
                            String.format("Duplicate primary key violation.Column: %s, Value: %s", pk, newValue));
                }
            }
        }
    }

    private static Map<String, Object> prepareRow(List<String> values, List<String> columns) {
        Map<String, Object> row = new HashMap<>();
        if (values.size() != columns.size()) {
            throw new QueryErrorException("Number of columns and values mismatched");
        }
        int i = 0;
        while (i < values.size()) {
            row.put(columns.get(i), values.get(i));
            i++;
        }
        return row;
    }

    private static List<String> getColumns(Map<String, String> columnAndItsTypes) {
        Set<String> columnSet = columnAndItsTypes.keySet();
        return new ArrayList<>(columnSet);
    }

    public static void displayTableContent(String tableName, List<String> columns, List<Condition> conditions,
                                           Operator operator, boolean isAllColumn) throws IOException, ClassNotFoundException {
        Database database = StateManager.getCurrentDB();
        Table table = database.getTable(tableName);
        if (table == null) {
            throw new QueryErrorException(String.format("Table: %s doesn't exist under the database: %s", tableName,
                    database.getDatabaseName()));
        }
        String tableId = table.getTableId();
        if (isAllColumn) {
            Map<String, String> columnMap = table.getColumnAndItsTypes();
            columns = getColumns(columnMap);
        }
        updateRam();
        TableContent tableContent;
        if (tableIdToTableContentMap.containsKey(tableId)) {
            tableContent = tableIdToTableContentMap.get(tableId);
        } else {
            System.out.println("No data");
            return;
        }
        String header = Printer.printRow(columns);
        System.out.println(Printer.separator(header));
        System.out.println(header);
        System.out.println(Printer.separator(header));
        List<Map<String, Object>> rows = tableContent.getRows();
        for (Map<String, Object> oneRow : rows) {
            boolean doesRowQualify = checkIfRowQualify(oneRow, conditions, operator);
            if (!doesRowQualify) {
                continue;
            }
            List<String> values = new ArrayList<>();
            for (String column : columns) {
                Object value = oneRow.get(column);
                if (value == null) {
                    values.add(" ");
                } else {
                    values.add(value.toString());
                }
            }
            String rowsFormatted = Printer.printRow(values);
            System.out.println(rowsFormatted);
        }
    }

    public static void updateTable(String tableName, List<Condition> conditions, Operator operator,
                                   List<String> columnNames, List<String> values) throws IOException, ClassNotFoundException {
        if (columnNames.size() != values.size()) {
            throw new QueryErrorException("Number of column and its corresponding values mismatched");
        }
        Database database = StateManager.getCurrentDB();
        Table table = database.getTable(tableName);
        if (table == null) {
            throw new QueryErrorException(String.format("Table: %s doesn't exist under the database: %s", tableName,
                    database.getDatabaseName()));
        }
        String tableId = table.getTableId();
        updateRam();
        if (LockManager.isTableLocked(tableId)) {
            throw new QueryErrorException("Table is locked");
        }
        LockManager.lockTableIfTransactionIsOn(tableId);
        TableContent tableContent;
        if (tableIdToTableContentMap.containsKey(tableId)) {
            tableContent = tableIdToTableContentMap.get(tableId);
        } else {
            System.out.println("Table is empty");
            return;
        }
        List<Map<String, Object>> rows = tableContent.getRows();
        for (Map<String, Object> oneRow : rows) {
            if (!checkIfRowQualify(oneRow, conditions, operator)) {
                continue;
            }
            int columnNumber = 0;
            for (String column : columnNames) {
                if (!oneRow.containsKey(column)) {
                    throw new RuntimeException("Invalid column: " + column);
                }
                oneRow.put(columnNames.get(columnNumber), values.get(columnNumber));
                columnNumber++;
            }
        }
        tableContent.setRows(rows);
        updateDisk();
        Printer.printSuccess("Table updated");
    }

    public static void deleteRows(String tableName, List<Condition> conditions, Operator operator)
            throws IOException, ClassNotFoundException {
        Database db = StateManager.getCurrentDB();
        Table table = db.getTable(tableName);
        String tableId = table.getTableId();
        updateRam();
        if (LockManager.isTableLocked(tableId)) {
            throw new QueryErrorException("Table is locked");
        }
        LockManager.lockTableIfTransactionIsOn(tableId);
        TableContent tableContent;
        if (tableIdToTableContentMap.containsKey(tableId)) {
            tableContent = tableIdToTableContentMap.get(tableId);
        } else {
            System.out.println("No data");
            return;
        }
        List<Map<String, Object>> rows = tableContent.getRows();
        int currentRow = 0;
        List<Integer> rowNumberToBeDeleted = new ArrayList<>();
        for (Map<String, Object> oneRow : rows) {
            boolean doesRowQualify = checkIfRowQualify(oneRow, conditions, operator);
            if (!doesRowQualify) {
                currentRow++;
                continue;
            }
            rowNumberToBeDeleted.add(currentRow);
            currentRow++;
        }
        if (rowNumberToBeDeleted.size() == 0) {
            Printer.printSuccess("0 row deleted");
            return;
        }
        rowNumberToBeDeleted.sort(Collections.reverseOrder());
        for (int index : rowNumberToBeDeleted) {
            rows.remove(index);
        }
        tableContent.setRows(rows);
        updateDisk();
        Printer.printSuccess(String.format("%d row deleted", rowNumberToBeDeleted.size()));
    }

    private static boolean checkIfRowQualify(Map<String, Object> oneRow, List<Condition> conditions,
                                             Operator operator) {
        if (conditions.size() == 0) {
            return true;
        }
        boolean[] conditionTestResult = new boolean[conditions.size()];
        for (int i = 0; i < conditions.size(); i++) {
            String column1 = conditions.get(i).getColumn();
            String columnValue1 = conditions.get(i).getColumnValue();
            SIGN sign = conditions.get(i).getOperator();
            if (!oneRow.containsKey(column1)) {
                throw new QueryErrorException("Column doesn't exist: " + column1);
            }
            String actualValue = oneRow.get(column1).toString();
            if (sign == SIGN.EQUAL) {
                conditionTestResult[i] = columnValue1.equals(actualValue);
            } else if (sign == SIGN.NOT_EQUAL) {
                conditionTestResult[i] = !columnValue1.equals(actualValue);
            } else if (sign == SIGN.GREATER_THAN) {
                conditionTestResult[i] = columnValue1.compareTo(actualValue) < 0;
            } else if (sign == SIGN.LESSER_THAN) {
                conditionTestResult[i] = columnValue1.compareTo(actualValue) > 0;
            } else if (sign == SIGN.GREATER_THAN_EQUAL_TO) {
                conditionTestResult[i] = columnValue1.compareTo(actualValue) <= 0;
            } else if (sign == SIGN.LESSER_THAN_EQUAL_TO) {
                conditionTestResult[i] = columnValue1.compareTo(actualValue) >= 0;
            } else {
                throw new QueryErrorException("Operator not supported yet: " + operator.toString());
            }
        }
        if (conditions.size() == 1) {
            return conditionTestResult[0];
        } else {
            if (operator == Operator.AND) {
                return conditionTestResult[0] && conditionTestResult[1];
            } else if (operator == Operator.OR) {
                return conditionTestResult[0] || conditionTestResult[1];
            } else {
                throw new RuntimeException("Invalid syntax");
            }
        }
    }

    public static void truncateTable(String tableName) throws IOException, ClassNotFoundException {
        Database database = StateManager.getCurrentDB();
        Table table = database.getTable(tableName);
        if (table == null) {
            throw new QueryErrorException(String.format("Table: %s doesn't exist under the database: %s", tableName,
                    database.getDatabaseName()));
        }
        String tableId = table.getTableId();
        if (LockManager.isTableLocked(tableId)) {
            throw new QueryErrorException("Table is locked");
        }
        LockManager.lockTableIfTransactionIsOn(tableId);
        TableContent tableContent;
        if (tableIdToTableContentMap.containsKey(tableId)) {
            tableContent = tableIdToTableContentMap.get(tableId);
        } else {
            return;
        }
        tableContent.deleteAllRows();
        updateDisk();
    }

    public static void startTransaction() {
        StateManager.is_transaction_on = true;
        Printer.printSuccess("TRANSACTION STARTED");
    }

    public static void commitTransaction() throws IOException {
        StateManager.is_transaction_on = false;
        updateDisk();
        LockManager.unlockAllTable();
        Printer.printSuccess("TRANSACTION COMMITTED");
    }

    public static void rollBackTransaction() throws IOException, ClassNotFoundException {
        StateManager.is_transaction_on = false;
        updateRam();
        LockManager.unlockAllTable();
        Printer.printSuccess("TRANSACTION ROLLBACK");
    }

    private static void updateDisk() throws IOException {
        if (StateManager.is_transaction_on) {
            return;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(contentFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(tableIdToTableContentMap);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    private static void updateRam() throws IOException, ClassNotFoundException {
        File file = new File(contentFile);
        if (file.exists() && file.length() == 0) {
            tableIdToTableContentMap = new HashMap<>();
            return;
        }
        if (StateManager.is_transaction_on) {
            return;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        tableIdToTableContentMap = (Map<String, TableContent>) objectInputStream.readObject();
        objectInputStream.close();
    }

    private static void checkColumnContentAndColumnType(List<String> columnTypeList, List<String> columnValueList) {
        int index = 0;
        for (index = 0; index < columnTypeList.size(); index++) {
            String columnValue = columnValueList.get(index);
            String columnType = columnTypeList.get(index);
            Helper.isColumnTypeSupported(columnType);
            if (columnType.equalsIgnoreCase("string")) {
                Helper.isString(columnValue);
            } else if (columnType.equalsIgnoreCase("boolean")) {
                Helper.isBoolean(columnValue);
            } else if (columnType.equalsIgnoreCase("integer")) {
                Helper.isInteger(columnValue);
            } else if (columnType.equalsIgnoreCase("double")) {
                Helper.isDouble(columnValue);
            } else {
                throw new QuerySyntaxException("Not a supported datatype:" + columnType);
            }
        }
    }

    public static void checkForeignKeyViolation(Table table, Map<String, Object> row) {
        List<String> fkValues = table.getForeignKeysForeignTableAndColumn();
        if (fkValues.isEmpty()) {
            return;
        }
        String foreignKey = fkValues.get(0);
        String foreignTableName = fkValues.get(1);
        String foreignColumn = fkValues.get(2);
        Database currentDB = StateManager.getCurrentDB();
        Table foreignTable = currentDB.getTable(foreignTableName);
        if (!tableIdToTableContentMap.containsKey(foreignTable.getTableId())) {
            throw new QueryErrorException(String.format(
                    "Foreign key violation: Foreign table: %s doesn't have value under column: %s for the specified in the foreignKey: %s", foreignTableName, foreignColumn, foreignKey));
        }
        TableContent foreignTableContent = tableIdToTableContentMap.get(foreignTable.getTableId());
        List<Map<String, Object>> foreignTableRows = foreignTableContent.getRows();
        String hostTableForeignKeyValue = row.get(foreignKey).toString();
        for (Map<String, Object> foreignTableRow : foreignTableRows) {
            String foreignTableFKValue = foreignTableRow.get(foreignColumn).toString();
            if (hostTableForeignKeyValue.equals(foreignTableFKValue)) {
                return;
            }
        }
        throw new QueryErrorException(String.format(
                "Foreign key violation: Foreign table: %s doesn't have value under column: %s for the specified in the foreignKey: %s", foreignTableName, foreignColumn, foreignKey));
    }
}
