package com.devrajs.tinydb.queryParser.table;

import com.devrajs.tinydb.common.*;
import com.devrajs.tinydb.exception.QuerySyntaxException;
import com.devrajs.tinydb.manager.DBContents;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UpdateTable {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public UpdateTable(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws IOException, ClassNotFoundException {
        int index = 0;
        TokensValidator validator = new TokensValidator(tokenList);
        validator.add("update", index).validate();
        String tableName = tokenList.get(index + 1);
        validator.add("set", index + 2).validate();
        List<String> columns = new ArrayList<>();
        List<String> columnValues = new ArrayList<>();

        int i = index + 3;
        List<Condition> conditions = new ArrayList<>();
        Operator operator = null;
        while (i < tokenList.size()) {
            String column = tokenList.get(i++);
            columns.add(column);
            validator.add("=", i++).validate();
            String value = tokenList.get(i++);
            columnValues.add(value);
            if (tokenList.get(i).equalsIgnoreCase(",")) {
                i++;
                continue;
            } else if (tokenList.get(i).equalsIgnoreCase("where")) {
                i++;
                String conditionColumnName1 = tokenList.get(i++);
                validator.add("=", i).validate();
                SIGN mathOperator1 = MathOperators.valueOf(tokenList.get(i++));
                String columnValue1 = tokenList.get(i++);
                conditions.add(new Condition(conditionColumnName1, columnValue1, mathOperator1));
                if (!tokenList.get(i).equals(";")) {
                    if (tokenList.get(i).equalsIgnoreCase("and")) {
                        operator = Operators.valueOf(tokenList.get(i++));
                    } else if (tokenList.get(i).equalsIgnoreCase("or")) {
                        operator = Operators.valueOf(tokenList.get(i++));
                    } else {
                        throw new QuerySyntaxException("Invalid syntax");
                    }
                    String conditionColumnName2 = tokenList.get(i++);
                    validator.add("=", i).validate();
                    SIGN mathOperator2 = MathOperators.valueOf(tokenList.get(i++));
                    String columnValue2 = tokenList.get(i++);
                    conditions.add(new Condition(conditionColumnName2, columnValue2, mathOperator2));
                    break;
                } else if (tokenList.get(i).equalsIgnoreCase(";")) {
                    break;
                }
            } else if (tokenList.get(i).equalsIgnoreCase(";")) {
                break;
            }
        }
        DBContents.updateTable(tableName, conditions, operator, columns, columnValues);
    }
}
