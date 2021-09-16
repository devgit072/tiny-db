package com.devrajs.tinydb.manager;

import com.devrajs.tinydb.model.Database;
import com.devrajs.tinydb.model.User;

import java.util.UUID;

public class StateManager {
    public static final int SLEEP_TIME = 0;
    public static boolean is_transaction_on = false;
    public static String currentSession="";
    // Details of current loggedin user.
    private static User currentUser;
    // Details of current database which user has selected.
    private static Database currentDB;

    public static User getCurrentUser() {
        if(currentUser == null) {
            throw new RuntimeException("User is not logged in. Please login first");
        }
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        StateManager.currentUser = currentUser;
    }

    public static Database getCurrentDB() {
        if (currentDB == null) {
            throw new RuntimeException("No database selected. Please switch to any DB");
        }
        return currentDB;
    }

    public static void setCurrentDB(Database currentDB) {
        StateManager.currentDB = currentDB;
    }

    public static void setCurrentSession() {
        if(currentSession.isEmpty()) {
            currentSession = UUID.randomUUID().toString();
        }
    }
    public static String getetCurrentSession() {
        return currentSession;
    }
}
