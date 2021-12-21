package com.devrajs.tinydb.queryParser.transaction;

import com.devrajs.tinydb.exception.QuerySyntaxException;
import com.devrajs.tinydb.manager.DBContents;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.util.List;

public class RollbackTransaction {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public RollbackTransaction(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws IOException, ClassNotFoundException {
        if (tokenList.size() != 3) {
            throw new QuerySyntaxException("Invalid syntax");
        }
        int index = 0;
        TokensValidator validator = new TokensValidator(tokenList);
        validator.add("rollback", index).add("transaction", index + 1).validate();
        DBContents.rollBackTransaction();
    }
}
