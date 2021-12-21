package com.devrajs.tinydb.queryParser.table;

import com.devrajs.tinydb.exception.QuerySyntaxException;
import com.devrajs.tinydb.manager.DBMetadata;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class DropTable {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public DropTable(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        if (tokenList.size() != 4) {
            throw new QuerySyntaxException("Invalid syntax");
        }
        int index = 0;
        TokensValidator validator = new TokensValidator(tokenList);
        validator.add("drop", index).add("table", index + 1).validate();
        String tableName = tokenList.get(index + 2);
        DBMetadata.getInstance().dropTable(tableName);
        System.out.println("Table deleted successfully");
    }
}
