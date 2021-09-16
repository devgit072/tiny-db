package com.devrajs.tinydb.manager;

import java.io.IOException;

import static com.devrajs.tinydb.common.FileHelper.createDirectory;
import static com.devrajs.tinydb.common.FileHelper.createFile;
import static com.devrajs.tinydb.manager.FileConstants.*;

public class FileManager {

    public static void createAllDirectoriesAndFiles() throws IOException {
        createLogDirAndFiles();
        createDumpsDirAndFiles();
        createERDDirectoryAndFiles();
    }

    private static void createLogDirAndFiles() throws IOException {
        createDirectory(LOG_DIR);
        createDirectory(SQL_DUMP_DIR);
        createFile(ERROR_LOG);
        createFile(GENERAL_QUERY_LOG);
        createFile(TRANSACTION_LOG);
    }

    private static void createDumpsDirAndFiles() throws IOException {
        createDirectory(DUMP_DIR);
        createFile(CONTENT_FILE);
        createFile(LOCK_FILE);
        createFile(METADATA_FILE);
    }

    private static void createERDDirectoryAndFiles() {
        createDirectory(ERD_DIR);
    }
}