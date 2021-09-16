package com.devrajs.tinydb.common;

public class Condition {
    private final String column;
    private final String columnValue;
    private final SIGN operator;

    public Condition(String column, String columnValue, SIGN operator) {
        this.column = column;
        this.columnValue = columnValue;
        this.operator = operator;
    }

    public String getColumn() {
        return column;
    }

    public String getColumnValue() {
        return columnValue;
    }

    public SIGN getOperator() {
        return operator;
    }
}
