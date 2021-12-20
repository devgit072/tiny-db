package com.devrajs.tinydb.tokens;

import java.util.ArrayList;
import java.util.List;

public class TokensValidator {
    private record TokenAndPosition(String token, int position){}
    List<TokenAndPosition> tokenAndPositionList;
    List<String> actualTokens;
    public TokensValidator(List<String> actualTokens) {
        tokenAndPositionList = new ArrayList<>();
        this.actualTokens = actualTokens;
    }

    private void checkTokenValidity(List<String> tokenList, int index, String expectedToken) {
        String token = tokenList.get(index);
        String errorMsg = String.format("Token: %s not found at index: %d", expectedToken, index);
        if (!token.equalsIgnoreCase(expectedToken)) {
            throw new RuntimeException("Invalid syntax: " + errorMsg);
        }
    }

    public void validate() {
        for(TokenAndPosition t : tokenAndPositionList) {
            int position = t.position();
            String token = t.token();
            checkTokenValidity(actualTokens, position, token);
        }
    }

    public TokensValidator add(String token, int position) {
        TokenAndPosition t = new TokenAndPosition(token, position);
        tokenAndPositionList.add(t);
        return this;
    }
}
