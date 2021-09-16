package com.devrajs.tinydb.queryParser.user;

import com.devrajs.tinydb.common.Util;
import com.devrajs.tinydb.manager.DBMetadata;
import com.devrajs.tinydb.model.User;
import com.devrajs.tinydb.queries.QueryProcessor;
import com.devrajs.tinydb.tokens.TokensValidator;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/*
create user with (ben, ben123);
 */
public class CreateUser {
    QueryProcessor queryProcessor;
    List<String> tokenList;

    public CreateUser(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
        this.tokenList = queryProcessor.getTokenList();
    }

    public void processTokens() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        if (tokenList.size() != 9) {
            throw new RuntimeException("Invalid syntax");
        }

        int index = 0;
        TokensValidator tokensValidator = new TokensValidator(tokenList);
        tokensValidator.add("create", 0).add("user", 1).add("with", 2).add("(", 3).add(",", 5).add(")", 7)
                .validate();
        String userName = tokenList.get(index + 4);
        String password = tokenList.get(index + 6);
        String sha1Password = Util.getSha1(password);
        System.out.println("Security question: Enter your pet name: ");
        String securityAns = queryProcessor.getInputs().getStringInput();
        User user = new User(userName, sha1Password, securityAns);
        DBMetadata dbMetadata = DBMetadata.getInstance();
        dbMetadata.createUser(user);
    }
}
