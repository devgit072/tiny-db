package com.devrajs.tinydb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Table implements Serializable {
    String parentDatabase;
    String tableName;
    String tableId;
    Map<String, String> columnAndItsTypes;
    List<String> primaryKeyColumns;
    List<String> foreignKeysForeignTableAndColumn;

    public Table() {
    }

    public Table(String tableName, Map<String, String> columnAndItsTypes, List<String> primaryKeyColumns,
                 List<String> foreignKeysForeignTableAndColumn) {
        this.tableName = tableName;
        this.columnAndItsTypes = columnAndItsTypes;
        this.primaryKeyColumns = primaryKeyColumns;
        this.foreignKeysForeignTableAndColumn = foreignKeysForeignTableAndColumn;
        tableId = UUID.randomUUID().toString();
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableId() {
        return tableId;
    }

    public Map<String, String> getColumnAndItsTypes() {
        return columnAndItsTypes;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public void setColumnAndItsTypes(Map<String, String> columnAndItsTypes) {
        this.columnAndItsTypes = columnAndItsTypes;
    }

    public List<String> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }

    public List<String> getForeignKeysForeignTableAndColumn() {
        return foreignKeysForeignTableAndColumn;
    }

    public String getParentDatabase() {
        return parentDatabase;
    }

    public void setParentDatabase(String parentDatabase) {
        this.parentDatabase = parentDatabase;
    }

    public void setPrimaryKeyColumns(List<String> primaryKeyColumns) {
        this.primaryKeyColumns = primaryKeyColumns;
    }

    public void setForeignKeysForeignTableAndColumn(List<String> foreignKeysForeignTableAndColumn) {
        this.foreignKeysForeignTableAndColumn = foreignKeysForeignTableAndColumn;
    }

    public String asString() {
        String columns = "";
        boolean flag = true;
        List<String> pks = getPrimaryKeyColumns();
        String foreignKey = "";
        if (!foreignKeysForeignTableAndColumn.isEmpty()) {
            foreignKey = foreignKeysForeignTableAndColumn.get(0);
        }
        for (Map.Entry<String, String> entry : columnAndItsTypes.entrySet()) {
            String columnName = entry.getKey();
            String columnType = entry.getValue();
            boolean isPK = isPresentInTheList(columnName, pks);
            boolean isFK = foreignKey.equals(columnName);
            String pkStr = "";
            String fkStr = "";
            if(isPK) {
                pkStr = " primary key";
            } else if(isFK) {
                fkStr = " foreign key";
            }
            if (flag) {
                columns = String.format("%s(%s)%s%s", columnName, columnType, pkStr, fkStr);
            } else {
                columns = String.format("%s , %s(%s)%s%s", columns, columnName, columnType, pkStr, fkStr);
            }
            flag = false;
        }
        return String.format("%s [%s]", tableName, columns);
    }

    boolean isPresentInTheList(String key, List<String> list) {
        for (String val : list) {
            if (val.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasColumn(String columnNameToBeChecked) {
        return columnAndItsTypes.containsKey(columnNameToBeChecked);
    }

    public List<String> getColumnTypes() {
        return new ArrayList<>(columnAndItsTypes.values());
    }

    public String toString() {
        return "[parentDatabase:" + parentDatabase + ",tableName:" + tableName + ",tableId:" + tableId
                + ",columnAndItsTypes:" + columnAndItsTypes.toString() + ",primaryKeyColumns:"
                + primaryKeyColumns.toString() + ",foreignKeysForeignTableAndColumn:"
                + foreignKeysForeignTableAndColumn.toString() + "]";
    }
}