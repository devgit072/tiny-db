package com.devrajs.tinydb.common;

public class AlterTableParameters {
    private String newColumnName;
    private String oldColumnName;
    private String operation;
    private String newColumnType;

    public String getNewColumnName() {
        return newColumnName;
    }

    public void setNewColumnName(String newColumnName) {
        this.newColumnName = newColumnName;
    }

    public String getOldColumnName() {
        return oldColumnName;
    }

    public void setOldColumnName(String oldColumnName) {
        this.oldColumnName = oldColumnName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getNewColumnType() {
        return newColumnType;
    }

    public void setNewColumnType(String newColumnType) {
        this.newColumnType = newColumnType;
    }

    @Override
    public String toString() {
        return "AlterTableParameters{" +
                "newColumnName='" + newColumnName + '\'' +
                ", oldColumnName='" + oldColumnName + '\'' +
                ", operation='" + operation + '\'' +
                ", newColumnType='" + newColumnType + '\'' +
                '}';
    }
}
