package com.devrajs.tinydb.queryParser.dump;

import com.devrajs.tinydb.common.Printer;
import com.devrajs.tinydb.manager.DumpManager;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class SourceDump {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public SourceDump(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        if (tokenList.size() != 5) {
            throw new RuntimeException("Invalid syntax");
        }

        int index = 0;
        TokensValidator validator = new TokensValidator(tokenList);
        validator.add("source", index).add("dump", index + 1).validate();
        String fileName = tokenList.get(index + 2);
        String databaseName = tokenList.get(index + 3);
        DumpManager dumpManager = new DumpManager();
        dumpManager.sourceDump(fileName, databaseName);
        Printer.printSuccess("Database dump restored successfully");
    }
}
