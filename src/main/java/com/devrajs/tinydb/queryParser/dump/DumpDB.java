package com.devrajs.tinydb.queryParser.dump;

import com.devrajs.tinydb.common.Printer;
import com.devrajs.tinydb.exception.QuerySyntaxException;
import com.devrajs.tinydb.manager.DumpManager;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.util.List;

public class DumpDB {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public DumpDB(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws IOException, ClassNotFoundException {
        if (tokenList.size() != 5 && tokenList.size() != 6) {
            throw new QuerySyntaxException("Invalid syntax");
        }

        int index = 0;
        TokensValidator validator = new TokensValidator(tokenList);
        validator.add("dump", index).add("database", index + 1).validate();
        String databaseName = tokenList.get(index + 2);
        String withDataStr = tokenList.get(index + 3);
        String filePath = "";
        if(tokenList.size() == 6) {
            filePath = tokenList.get(index+4);
        }
        boolean withData = Boolean.parseBoolean(withDataStr);
        DumpManager dumpManager = new DumpManager();
        dumpManager.dumpDatabase(databaseName, withData, filePath);
        Printer.printSuccess("Database dump taken successfully");
    }
}
