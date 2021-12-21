package com.devrajs.tinydb.queryParser.database;

import com.devrajs.tinydb.common.Printer;
import com.devrajs.tinydb.exception.QuerySyntaxException;
import com.devrajs.tinydb.manager.DBMetadata;
import com.devrajs.tinydb.manager.StateManager;
import com.devrajs.tinydb.model.Database;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.util.List;
/*
create database 'testDB';
 */
public class CreateDB {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public CreateDB(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws IOException, ClassNotFoundException {
        if (tokenList.size() != 4) {
            throw new QuerySyntaxException("Invalid syntax");
        }

        int index = 0;
        TokensValidator tokensValidator = new TokensValidator(tokenList);
        tokensValidator.add("create", 0).add("database", 1).validate();
        String databaseName = tokenList.get(index + 2);
        Database database = new Database(StateManager.getCurrentUser().getUserName(), databaseName);
        DBMetadata.getInstance().createDB(database);
        Printer.printSuccess("Database created successfully");
    }
}
