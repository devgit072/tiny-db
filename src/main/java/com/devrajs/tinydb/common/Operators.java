package com.devrajs.tinydb.common;

public class Operators {
    public static Operator valueOf(String val) {
        if(val.equalsIgnoreCase("and")) {
            return Operator.AND;
        } else if(val.equalsIgnoreCase("or")) {
            return Operator.OR;
        } else {
            throw new RuntimeException("Invalid value of operator: " + val);
        }
    }
}