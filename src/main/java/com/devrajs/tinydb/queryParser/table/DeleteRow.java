package com.devrajs.tinydb.queryParser.table;

import com.devrajs.tinydb.common.*;
import com.devrajs.tinydb.exception.QuerySyntaxException;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.devrajs.tinydb.manager.DBContents.deleteRows;
import static com.devrajs.tinydb.manager.DBContents.truncateTable;

public class DeleteRow {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public DeleteRow(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws IOException, ClassNotFoundException {
        int index = 0;
        TokensValidator validator = new TokensValidator(tokenList);
        validator.add("delete", index).add("from", index + 1).validate();
        String tableName = tokenList.get(index + 2);
        if (tokenList.get(index + 3).equals(";")) {
            truncateTable(tableName);
            Printer.success();
            return;
        }
        int i = index + 3;
        List<Condition> conditions = new ArrayList<>();
        Operator operator = null;
        if (tokenList.get(i).equals("where")) {
            i++;
            String conditionColumnName1 = tokenList.get(i++);
            SIGN operator1 = MathOperators.valueOf(tokenList.get(i++));
            String columnValue1 = tokenList.get(i++);
            conditions.add(new Condition(conditionColumnName1, columnValue1, operator1));
            if (!tokenList.get(i).equals(";")) {
                if (tokenList.get(i).equalsIgnoreCase("and")) {
                    operator = Operators.valueOf(tokenList.get(i++));
                } else if (tokenList.get(i).equalsIgnoreCase("or")) {
                    operator = Operators.valueOf(tokenList.get(i++));
                } else {
                    throw new RuntimeException("Invalid syntax");
                }
                String conditionColumnName2 = tokenList.get(i++);
                SIGN operator2 = MathOperators.valueOf(tokenList.get(i++));
                String columnValue2 = tokenList.get(i);
                conditions.add(new Condition(conditionColumnName2, columnValue2, operator2));
            }
        } else {
            throw new QuerySyntaxException("Invalid syntax");
        }

        deleteRows(tableName, conditions, operator);
    }
}
