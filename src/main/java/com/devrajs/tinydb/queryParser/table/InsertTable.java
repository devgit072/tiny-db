package com.devrajs.tinydb.queryParser.table;

import com.devrajs.tinydb.exception.QuerySyntaxException;
import com.devrajs.tinydb.manager.DBContents;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InsertTable {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public InsertTable(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws IOException, ClassNotFoundException {
        int index = 0;
        TokensValidator validator = new TokensValidator(tokenList);
        validator.add("insert", index).add("into", index + 1).validate();
        String tableName = tokenList.get(index + 2);
        validator.add("values", index+3).validate();
        validator.add("(", index + 4).validate();
        int i = index + 5;
        List<String> columnValues = new ArrayList<>();
        while (i < tokenList.size()) {
            String columnValue = tokenList.get(i++);
            columnValues.add(columnValue);
            if (tokenList.get(i).equals(")")) {
                i++;
                break;
            } else if (tokenList.get(i).equals(",")) {
                i++;
            } else {
                throw new QuerySyntaxException("Invalid syntax");
            }
        }
        DBContents.insertRowInTable(tableName, columnValues);
        System.out.println("Row inserted successfully");
    }
}
