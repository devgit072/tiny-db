package com.devrajs.tinydb.queryParser.table;

import com.devrajs.tinydb.common.AlterTableParameters;
import com.devrajs.tinydb.manager.DBMetadata;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.util.List;

public class AlterTable {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public AlterTable(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws IOException, ClassNotFoundException {
        int index = 0;
        TokensValidator validator = new TokensValidator(tokenList);
        validator.add("alter", index).add("table", index + 1).validate();
        String tableName = tokenList.get(index + 2);
        int i = index + 3;
        String userOp = tokenList.get(i++);
        AlterTableParameters alterTableParameters = new AlterTableParameters();
        if (userOp.equalsIgnoreCase("add")) {
            alterTableParameters.setOperation(userOp);
            validator.add("column", i++).validate();
            String newColumnName = tokenList.get(i++);
            String newColumnType = tokenList.get(i++);
            alterTableParameters.setNewColumnName(newColumnName);
            alterTableParameters.setNewColumnType(newColumnType);
        } else if (userOp.equalsIgnoreCase("drop")) {
            alterTableParameters.setOperation(userOp);
            validator.add("column", i++).validate();
            String columnToBeDeleted = tokenList.get(i++);
            alterTableParameters.setOldColumnName(columnToBeDeleted);
        } else if (userOp.equalsIgnoreCase("rename")) {
            throw new RuntimeException("Not yet implemented");
        } else {
            throw new RuntimeException("Invalid syntax");
        }
        DBMetadata.getInstance().alterTable(tableName, alterTableParameters);
        System.out.println("Table altered successfully");
    }
}
