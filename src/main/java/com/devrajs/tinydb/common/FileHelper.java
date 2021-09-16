package com.devrajs.tinydb.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelper {
    public static void createFile(String fileName) throws IOException {
        File file = new File(fileName);
        if(!file.exists()) {
            if(!file.createNewFile()) {
                throw new RuntimeException("File creation unsuccessful");
            }
        }
    }

    public static void createDirectory(String dirName) {
        File file = new File(dirName);
        if(!file.exists()) {
            if(!file.mkdir()) {
                throw new RuntimeException("File creation unsuccessful");
            }
        }
    }

    public static void writeIntofile(String filePath, String content) throws IOException {
        FileWriter fileWriter = new FileWriter(filePath, true);
        fileWriter.write(content);
        fileWriter.close();
    }
}
