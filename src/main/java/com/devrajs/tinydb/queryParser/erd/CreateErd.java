package com.devrajs.tinydb.queryParser.erd;

import com.devrajs.tinydb.exception.QueryErrorException;
import com.devrajs.tinydb.exception.QuerySyntaxException;
import com.devrajs.tinydb.manager.DBMetadata;
import com.devrajs.tinydb.manager.StateManager;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.util.List;

public class CreateErd {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public CreateErd(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws IOException, ClassNotFoundException {
        if (tokenList.size() != 4) {
            throw new QuerySyntaxException("Invalid syntax");
        }

        int index = 0;
        TokensValidator validator = new TokensValidator(tokenList);
        validator.add("create", index).add("erd", index + 1).validate();
        String databaseName = tokenList.get(index + 2);

        if (!databaseName.equals(StateManager.getCurrentDB().getDatabaseName())) {
            throw new QueryErrorException("Invalid database name");
        }

        DBMetadata.getInstance().createERD(databaseName);
    }
}
