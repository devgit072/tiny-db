package com.devrajs.tinydb.queryParser.database;

import com.devrajs.tinydb.manager.DBMetadata;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.util.List;

public class UseDB {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public UseDB(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws IOException, ClassNotFoundException {
        if (tokenList.size() != 3) {
            throw new RuntimeException("Invalid syntax");
        }
        int index = 0;
        TokensValidator tokensValidator = new TokensValidator(tokenList);
        tokensValidator.add("use", 0).validate();
        String databaseName = tokenList.get(index + 1);
        DBMetadata.getInstance().useDB(databaseName);
    }
}