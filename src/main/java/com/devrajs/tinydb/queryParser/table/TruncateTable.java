package com.devrajs.tinydb.queryParser.table;

import com.devrajs.tinydb.manager.DBContents;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/*
truncate table player;
 */
public class TruncateTable {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public TruncateTable(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        if (tokenList.size() != 4) {
            throw new RuntimeException("Invalid syntax");
        }

        int index = 0;
        TokensValidator validator = new TokensValidator(tokenList);
        validator.add("truncate", index).add("table", index + 1).validate();
        String tablename = tokenList.get(index + 2);
        DBContents.truncateTable(tablename);
        System.out.println("Table truncated successfully");
    }
}
