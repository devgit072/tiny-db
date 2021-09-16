package com.devrajs.tinydb.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Util {
    public static void printCursor() {
        System.out.print("NND>>> ");
    }

    public static String getSha1(String input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte[] result = messageDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static boolean isString(String word) {
        if (word.startsWith("'") && word.endsWith("'")) {
            return true;
        }
        if (word.startsWith("\"") && word.endsWith("\"")) {
            return true;
        }
        return false;
    }

    public static String getStringContent(String word) {
        return word.substring(1, word.length() - 1);
    }

    public static void printTokens(List<String> tokens) {
        for (String s : tokens) {
            System.out.print(s + "  <==>");
        }
    }
}
