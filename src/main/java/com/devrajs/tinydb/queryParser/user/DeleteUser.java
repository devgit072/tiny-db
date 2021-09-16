package com.devrajs.tinydb.queryParser.user;

import com.devrajs.tinydb.manager.DBMetadata;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.util.List;

/*
delete user ben;
 */
public class DeleteUser {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public DeleteUser(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws IOException, ClassNotFoundException {
        if (tokenList.size() != 4) {
            throw new RuntimeException("Invalid syntax");
        }
        int index = 0;
        TokensValidator tokensValidator = new TokensValidator(tokenList);
        tokensValidator.add("delete", 0).add("user", 1).validate();
        String userName = tokenList.get(index + 2);
        DBMetadata.getInstance().deleteUser(userName);
    }
}
