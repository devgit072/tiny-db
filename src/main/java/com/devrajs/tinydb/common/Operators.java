package com.devrajs.tinydb.common;

import com.devrajs.tinydb.exception.QuerySyntaxException;

public class Operators {
    public static Operator valueOf(String val) {
        if(val.equalsIgnoreCase("and")) {
            return Operator.AND;
        } else if(val.equalsIgnoreCase("or")) {
            return Operator.OR;
        } else {
            throw new QuerySyntaxException("Invalid value of operator: " + val);
        }
    }
}