package com.devrajs.tinydb.model;

import com.devrajs.tinydb.exception.QueryErrorException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Database implements Serializable {
    private String databaseId;
    private String userName;
    private List<Table> tableList;
    private String databaseName;

    public Database() {
    }

    public Database(String userName, String databaseName) {
        this.userName = userName;
        this.databaseName = databaseName;
        databaseId = UUID.randomUUID().toString();
        tableList = new ArrayList<>();
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public String getUserName() {
        return userName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTableList(List<Table> tableList) {
        this.tableList = tableList;
    }

    public void addTable(Table table) {
        if (doesTableExist(table.getTableName())) {
            throw new QueryErrorException(
                    String.format("Table: %s under database: %s already exists", table.getTableName(), databaseName));
        }
        doesItViolateForeignKey(table);
        tableList.add(table);
    }

    public void removeTable(String tableName) {
        if (!doesTableExist(tableName)) {
            throw new QueryErrorException(
                    String.format("Table: %s doesn't exists under database: %s", tableName, databaseName));
        }
        int index = -1;
        for (Table table : tableList) {
            if (table.getTableName().equals(tableName)) {
                index = tableList.indexOf(table);
            }
        }
        tableList.remove(index);
    }

    private boolean doesTableExist(String tableName) {
        for (Table t : tableList) {
            if (t.getTableName().equals(tableName)) {
                return true;
            }
        }
        return false;
    }

    private void doesItViolateForeignKey(Table newTableToBeAdded) {
        List<String> newForeignKeys = newTableToBeAdded.getForeignKeysForeignTableAndColumn();
        if (newForeignKeys.isEmpty()) {
            return;
        }
        String foreignKeyColumn = newForeignKeys.get(0);
        String foreignTableName = newForeignKeys.get(1);
        String foreignColumn = newForeignKeys.get(2);
        if(!doesTableExist(foreignTableName)) {
            throw new QueryErrorException(String.format("Foreign table: %s doesn't exists", foreignTableName));
        }
        Table foreignTable = getTable(foreignTableName);
        if(!foreignTable.hasColumn(foreignColumn)) {
            throw new QueryErrorException("Foreign column doesn't exists");
        }
        List<String> foreignTableePKs = foreignTable.getPrimaryKeyColumns();
        if (foreignTableePKs.isEmpty()) {
            throw new QueryErrorException(String.format("Foreign table: %s doesn't have any primary key.", foreignTableName));
        }
        // check is foreign key is primary key in foreign table.
        if(!isPresentInTheList(foreignColumn, foreignTableePKs)) {
            throw new QueryErrorException("Referenced foreign key is not primary key in the foreign table");
        }
    }
    boolean isPresentInTheList(String key, List<String> list) {
        for (String val : list) {
            if (val.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public List<Table> getTableList() {
        return tableList;
    }

    public Table getTable(String tableName) {
        for (Table t : tableList) {
            if (t.getTableName().equals(tableName)) {
                return t;
            }
        }
        return null;
    }

    public String toString() {
        String data = "(";
        data += "databaseId:" + databaseId + ",databaseName:" + databaseName + ",userName:" + userName + ",tableList:[";
        if (tableList.size() > 0) {
            for (Table table : tableList) {
                data += table.toString() + "::";
            }
            data = data.substring(0, data.length() - 1);
        }
        data += "])";
        return data;
    }
}