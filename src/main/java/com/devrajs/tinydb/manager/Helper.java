package com.devrajs.tinydb.manager;

import java.util.Map;

public class Helper {
    public static void isColumnTypeSupported(Map<String, String> columnAndItsTypes) {
        /*
         * There are only four data types supported in the NND DB. String, integer,
         * double and boolean
         */
        for (Map.Entry<String, String> entry : columnAndItsTypes.entrySet()) {
            String columnType = entry.getValue();
            isColumnTypeSupported(columnType);
        }
    }

    public static void isColumnTypeSupported(String columnType) {
        if (!(columnType.equalsIgnoreCase("integer") || columnType.equalsIgnoreCase("string")
                || columnType.equalsIgnoreCase("double") || columnType.equalsIgnoreCase("boolean"))) {
            throw new RuntimeException(String.format("Unsupported datatype: %s", columnType));
        }
    }

    public static void isString(String value) {
        if((value.startsWith("'") && value.endsWith("'")) ||
                (value.startsWith("\"") && value.endsWith("\""))) {
            return;
        }
        throw new RuntimeException(String.format("Value: %s is not a string value", value));
    }

    public static void isBoolean(String value) {
        if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return;
        }
        throw new RuntimeException(String.format("%s is not a valid boolean value", value));
    }

    public static void isInteger(String value) {
        try {
            Integer.parseInt(value);
        } catch (Exception e) {
            throw new RuntimeException(String.format("%s is not a valid integer value. Error: %s", value, e.getMessage()));
        }

    }

    public static void isDouble(String value) {
        try {
            Double.parseDouble(value);
        } catch (Exception e) {
            throw new RuntimeException(String.format("%s is not a valid double value. Error: %s", value, e.getMessage()));
        }
    }
}
