package com.devrajs.tinydb.queryParser.database;

import com.devrajs.tinydb.common.Printer;
import com.devrajs.tinydb.manager.DBMetadata;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/*
drop database 'testDB';
*/

public class DropDB {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public DropDB(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        if (tokenList.size() != 4) {
            throw new RuntimeException("Invalid syntax");
        }

        int index = 0;
        TokensValidator tokensValidator = new TokensValidator(tokenList);
        tokensValidator.add("drop", 0).add("database", 1).validate();
        String databaseName = tokenList.get(index + 2);
        DBMetadata.getInstance().dropDB(databaseName);
        Printer.printSuccess("Database dropped successfully");
    }
}
