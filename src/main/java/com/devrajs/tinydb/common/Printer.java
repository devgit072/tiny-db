package com.devrajs.tinydb.common;

import java.util.List;

public class Printer {
    public static final String CYAN_COLOR_CODE = "\u001B[46m";
    private final static String RED_COLOR_CODE = "\u001B[31m";
    private final static String GREEN_COLOR_CODE = "\u001B[32m";
    private final static String RESET_COLOR_CODE = "\u001B[0m";
    public static String printRow(List<String> values) {
        String rows = "";
        boolean flag = true;
        for(String o : values) {
            if(flag) {
                flag = false;
                rows = String.format("%s%-12s", rows, o);
            } else {
                rows = String.format("%s  |  %-12s", rows, o);
            }
        }
        return rows;
    }

    public static String separator(String val) {
        int len = val.length();
        StringBuilder str = new StringBuilder();
        str.append("-".repeat(len));
        return str.toString();
    }

    public static void printError(String errMsg) {
        System.out.println(RED_COLOR_CODE + "ERROR: " + errMsg + RESET_COLOR_CODE);
    }

    public static void printSuccess(String successMsg) {
        System.out.println(GREEN_COLOR_CODE + "SUCCESS: " + successMsg + RESET_COLOR_CODE);
    }

    public static void success() {
        System.out.println(GREEN_COLOR_CODE + "SUCCESS" + RESET_COLOR_CODE);
    }
}
