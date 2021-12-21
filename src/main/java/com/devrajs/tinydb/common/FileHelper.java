package com.devrajs.tinydb.common;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
    public static void createFile(String fileName) throws IOException {
        File file = new File(fileName);
        if(!file.exists()) {
            if(!file.createNewFile()) {
                throw new IOException("File creation unsuccessful");
            }
        }
    }

    public static void createDirectory(String dirName) throws IOException {
        File file = new File(dirName);
        if(!file.exists()) {
            if(!file.mkdir()) {
                throw new IOException("File creation unsuccessful");
            }
        }
    }

    public static void writeIntoFile(String filePath, String content) throws IOException {
        FileWriter fileWriter = new FileWriter(filePath, true);
        fileWriter.write(content);
        fileWriter.close();
    }

    public static void writeIntoFile(String filePath, List<String> contents) throws IOException {
        FileWriter fileWriter = new FileWriter(filePath, true);
        for(String content : contents) {
            fileWriter.write(content);
            fileWriter.write(System.lineSeparator());
        }

        fileWriter.close();
    }

    public static List<String> readFromFile(String filePath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String content = "";
        List<String> list = new ArrayList<>();
        while ((content = bufferedReader.readLine()) != null) {
            list.add(content);
        }
        return list;
    }
}
