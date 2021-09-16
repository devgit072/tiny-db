package com.devrajs.tinydb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableContent implements Serializable {
    private String tableId;
    private Map<String, String> columnAndItsTypes;
    private List<Map<String, Object>> rows;

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public Map<String, String> getColumnAndItsTypes() {
        return columnAndItsTypes;
    }

    public void setColumnAndItsTypes(Map<String, String> columnAndItsTypes) {
        this.columnAndItsTypes = columnAndItsTypes;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    public TableContent(String tableId, Map<String, String> columnAndItsTypes) {
        this.tableId = tableId;
        this.columnAndItsTypes = columnAndItsTypes;
        rows = new ArrayList<>();
    }

    public void addRow(Map<String, Object> oneRow) {
        rows.add(oneRow);
    }

    public void deleteRow(int index) {
        rows.remove(index);
    }

    public void deleteAllRows() {
        rows = new ArrayList<>();
    }
}
