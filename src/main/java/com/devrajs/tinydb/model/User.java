package com.devrajs.tinydb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User implements Serializable {
    private String userId;
    private String userName;
    private String password;

    private boolean isMultiFactorEnabled = false;

    private String securityAnswer;

    List<Database> databaseList = new ArrayList<>();

    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.isMultiFactorEnabled = false;
        userId = UUID.randomUUID().toString();
    }

    public User(String userName, String password, String securityAnswer) {
        this.userName = userName;
        this.password = password;
        this.securityAnswer = securityAnswer;
        this.isMultiFactorEnabled = true;
        userId = UUID.randomUUID().toString();
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void addDatabaseUnderUser(Database db) {
        String dbName = db.getDatabaseName();
        if (doesDBExist(dbName)) {
            throw new RuntimeException(String.format("Database: %s already exists under user: %s", dbName, userName));
        }
        databaseList.add(db);
    }

    public void removeDatabaseUnderUser(String dbName) {
        if (!doesDBExist(dbName)) {
            throw new RuntimeException(String.format("Database: %s doesn't exists under user: %s", dbName, userName));
        }
        int index = -1;
        for (Database database : databaseList) {
            if (database.getDatabaseName().equals(dbName)) {
                index = databaseList.indexOf(database);
            }
        }
        databaseList.remove(index);
    }

    public List<Database> getDatabaseList() {
        return databaseList;
    }

    public void setDatabaseList(List<Database> databaseList) {
        this.databaseList = databaseList;
    }

    public Database getDatabase(String dbName) {
        for (Database d : databaseList) {
            if (d.getDatabaseName().equals(dbName)) {
                return d;
            }
        }
        return null;
    }

    private boolean doesDBExist(String dbName) {
        for (Database d : databaseList) {
            if (d.getDatabaseName().equals(dbName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isMultiFactorEnabled() {
        return isMultiFactorEnabled;
    }

    public void setIsMultiFactorEnabled(boolean isMultiFactorEnabled) {
        this.isMultiFactorEnabled = isMultiFactorEnabled;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }
}
