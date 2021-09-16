package com.devrajs.tinydb.tokens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tokenizer class generates the tokens from the SQL query. It takes care of any
 * extra whitespace and ignores it. Except whitespace , everything is considered as
 * tokens. The string variable name wrapped inside double or single quotes which has whitespace will be
 * be considered as one single token. For example: "Hello world" is single token.
 * SQL query = SELECT * FROM employee where name='John Snow'; will have 9 tokens.
 * Token list= {SELECT , * , FROM, employee , where , name , = , 'John Snow' , ; };
 */

public class Tokenizer {
    private final String onsSingleQuery;

    public Tokenizer(String onsSingleQuery) {
        this.onsSingleQuery = onsSingleQuery;
    }

    /**
     * @return the list of tokens after generating the tokens from the SQL query.
     */
    public List<String> tokenizeQuery() {
        List<String> tokens = tokensOnWhiteSpace();
        tokens = splitTokensUsingChar(tokens, ',');
        tokens = splitTokensUsingChar(tokens, '(');
        tokens = splitTokensUsingChar(tokens, ')');
        tokens = splitTokensUsingChar(tokens, '=');
        tokens = splitTokensUsingChar(tokens, ';');

        tokens = removeEmptyTokens(tokens);
        tokens = trimAllTokens(tokens);
        return tokens;
    }

    /**
     * Split the SQL query using whitespace as delimiter.
     */
    private List<String> tokensOnWhiteSpace() {
        /*
        This regex will split the query separated by space, but it will not separate the word
        which is wrapped inside the single or double quotes.
         */
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
        Matcher matcher = regex.matcher(onsSingleQuery);
        List<String> tokens = new ArrayList<>();
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        List<String> tokensOnWhitespace = new ArrayList<>();
        for (String s : tokens) {
            if (s.length() == 0 || s.trim().equals("")) {
                continue;
            }
            tokensOnWhitespace.add(s.trim());
        }
        return tokensOnWhitespace;
    }

    private List<String> splitTokensUsingChar(List<String> tokenList, char ch) {
        List<String> processedTokens = new ArrayList<>();
        for (String s : tokenList) {
            if (isWordInsideQuotes(s)) {
                processedTokens.add(s);
            } else {
                List<String> list = tokenizeBasedOnSpecialChar(s, String.valueOf(ch));
                processedTokens.addAll(list);
            }
        }
        return processedTokens;
    }

    private List<String> removeEmptyTokens(List<String> tokenList) {
        List<String> processedTokens = new ArrayList<>();
        for(String token : tokenList) {
            if (!(token.length() == 0 || token.trim().equals(""))) {
                processedTokens.add(token);
            }
        }
        return processedTokens;
    }

    private List<String> trimAllTokens(List<String> tokenList) {
        List<String> processedTokens = new ArrayList<>();
        for(String token : tokenList) {
            processedTokens.add(token.trim());
        }
        return processedTokens;
    }

    private List<String> tokenizeBasedOnSpecialChar(String word, String del) {
        if (!word.contains(del)) {
            return Collections.singletonList(word);
        }
        if (word.equals(del)) {
            return Collections.singletonList(del);
        }
        List<String> tokenList = new ArrayList<>();
        String regex = del;
        if (del.equals("(")) {
            regex = "\\(";
        } else if (del.equals(")")) {
            regex = "\\)";
        }
        String[] tokens = word.split(regex);
        int index = 0;
        tokenList.add(tokens[index]);
        if (tokens.length == 1) {
            tokenList.add(del);
            return tokenList;
        }
        for (index = 1; index < tokens.length; index++) {
            tokenList.add(del);
            tokenList.add(tokens[index]);
        }
        if (word.endsWith(del)) {
            tokenList.add(del);
        }
        return tokenList;
    }

    // Return true if the word is wrapped inside single or double quotes.
    private boolean isWordInsideQuotes(String token) {
        return (token.startsWith("'") && token.endsWith("'")) ||
                (token.startsWith("\"") && token.endsWith("\""));
    }

    public static void main(String[] args) {
        String q = "insert into player values ('diego maradona', 30);";
        List<String> tokens = new Tokenizer(q).tokenizeQuery();
        int i = 0;
        for (String s : tokens) {
            System.out.print(s + "    ");
            i++;
        }
    }
}
