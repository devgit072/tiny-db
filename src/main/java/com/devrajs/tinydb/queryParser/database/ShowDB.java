package com.devrajs.tinydb.queryParser.database;

import com.devrajs.tinydb.exception.QuerySyntaxException;
import com.devrajs.tinydb.manager.DBMetadata;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.util.List;

/*
show databases;
*/

public class ShowDB {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public ShowDB(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws IOException, ClassNotFoundException {
        if (tokenList.size() != 3) {
            throw new QuerySyntaxException("Invalid syntax. Expected syntax: show database;");
        }
        TokensValidator tokensValidator = new TokensValidator(tokenList);
        tokensValidator.add("show", 0).add("databases", 1).validate();
        DBMetadata.getInstance().showDB();
    }
}