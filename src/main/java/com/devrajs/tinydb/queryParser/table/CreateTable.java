package com.devrajs.tinydb.queryParser.table;

import com.devrajs.tinydb.exception.QuerySyntaxException;
import com.devrajs.tinydb.manager.DBMetadata;
import com.devrajs.tinydb.model.Table;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CreateTable {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public CreateTable(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws IOException, ClassNotFoundException {
        int index = 0;
        TokensValidator validator = new TokensValidator(tokenList);
        validator.add("create", index).add("table", index + 1).validate();
        String tableName = tokenList.get(index + 2);
        validator.add("(", index + 3).validate();
        int i = index + 4;
        Map<String, String> columnAndItsTypes = new LinkedHashMap<>();
        List<String> primaryKeyColumns = new ArrayList<>();
        List<String> foreignKeysForeignTableAndColumn = new ArrayList<>();
        while (i < tokenList.size()) {
            String columnName = tokenList.get(i++);
            String type = tokenList.get(i++);
            columnAndItsTypes.put(columnName, type);
            if (tokenList.get(i).equalsIgnoreCase("primary")) {
                i++;
                validator.add("key", i++).validate();
                primaryKeyColumns.add(columnName);
            } else if (tokenList.get(i).equals("references")) {
                i++;
                String foreignTable = tokenList.get(i++);
                validator.add("(", i++).validate();
                String foreignColumn = tokenList.get(i++);
                validator.add(")", i++).validate();
                foreignKeysForeignTableAndColumn.add(columnName);
                foreignKeysForeignTableAndColumn.add(foreignTable);
                foreignKeysForeignTableAndColumn.add(foreignColumn);
            }
            if (tokenList.get(i).equals(")")) {
                i++;
                break;
            } else if (tokenList.get(i).equals(",")) {
                i++;
            } else {
                throw new QuerySyntaxException("Invalid syntax");
            }
        }
        Table table = new Table(tableName, columnAndItsTypes, primaryKeyColumns, foreignKeysForeignTableAndColumn);
        DBMetadata.getInstance().createTable(table);
        System.out.println("Table created successfully");
    }
}