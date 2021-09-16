package com.devrajs.tinydb.queryParser.transaction;

import com.devrajs.tinydb.manager.DBContents;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.util.List;

public class StartTransaction {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public StartTransaction(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws IOException, ClassNotFoundException {
        if (tokenList.size() != 3) {
            throw new RuntimeException("Invalid syntax");
        }
        TokensValidator validator = new TokensValidator(tokenList);
        validator.add("start", 0).add("transaction", 1).validate();
        DBContents.startTransaction();
    }
}
