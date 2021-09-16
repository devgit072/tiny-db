package com.devrajs.tinydb.common;

public class MathOperators {
    public static SIGN valueOf(String val) {
        if(val.equalsIgnoreCase("=")) {
            return SIGN.EQUAL;
        } else if(val.equalsIgnoreCase("!=")) {
            return SIGN.NOT_EQUAL;
        } else if(val.equalsIgnoreCase(">")) {
            return SIGN.GREATER_THAN;
        } else if(val.equalsIgnoreCase(">=")) {
            return SIGN.GREATER_THAN_EQUAL_TO;
        } else if(val.equalsIgnoreCase("<")) {
            return SIGN.LESSER_THAN;
        } else if(val.equalsIgnoreCase("<=")) {
            return SIGN.LESSER_THAN_EQUAL_TO;
        } else {
            throw new RuntimeException("Invalid operation: " + val);
        }
    }
}
