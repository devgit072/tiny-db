package com.devrajs.tinydb.queries;

import com.devrajs.tinydb.inputs.IInputs;
import com.devrajs.tinydb.queryParser.database.CreateDB;
import com.devrajs.tinydb.queryParser.database.DropDB;
import com.devrajs.tinydb.queryParser.database.ShowDB;
import com.devrajs.tinydb.queryParser.database.UseDB;
import com.devrajs.tinydb.queryParser.erd.CreateErd;
import com.devrajs.tinydb.queryParser.dump.DumpDB;
import com.devrajs.tinydb.queryParser.dump.SourceDump;
import com.devrajs.tinydb.queryParser.table.*;
import com.devrajs.tinydb.queryParser.transaction.CommitTrasaction;
import com.devrajs.tinydb.queryParser.transaction.RollbackTransaction;
import com.devrajs.tinydb.queryParser.transaction.StartTransaction;
import com.devrajs.tinydb.queryParser.user.CreateUser;
import com.devrajs.tinydb.queryParser.user.DeleteUser;
import com.devrajs.tinydb.queryParser.user.Userlogin;
import com.devrajs.tinydb.tokens.Tokenizer;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class QueryProcessor {
    String query;
    List<String> tokenList;

    private IInputs inputs = null;

    public QueryProcessor(String query) {
        this.query = query;
        validateSemicolonSuffix(query);
    }

    public QueryProcessor(String query, IInputs inputs) {
        this.query = query;
        this.inputs = inputs;
        validateSemicolonSuffix(query);
    }

    public List<String> getTokenList() {
        return tokenList;
    }

    public void parseQuery() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        Tokenizer tokenizer = new Tokenizer(query);
        tokenList = tokenizer.tokenizeQuery();
        processTokens(tokenList);
    }

    private void processTokens(List<String> tokens) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        int index = 0;

        String firstWord = tokens.get(index);
        String secondWord = tokens.get(index + 1);
        String firstTwoWord = String.format("%s %s", firstWord, secondWord);

        // TODO Remove hardcode.
        if (firstWord.equalsIgnoreCase("-u")) {
            new Userlogin(this).processTokens();
        } else if (firstWord.equalsIgnoreCase("select")) {
            new SelectTable(this).processTokens();
        } else if (firstTwoWord.equalsIgnoreCase("insert into")) {
            new InsertTable(this).processTokens();
        } else if (firstWord.equalsIgnoreCase("update")) {
            new UpdateTable(this).processTokens();
        } else if (firstTwoWord.equalsIgnoreCase("create table")) {
            new CreateTable(this).processTokens();
        } else if (firstTwoWord.equalsIgnoreCase("drop table")) {
            new DropTable(this).processTokens();
        } else if (firstTwoWord.equalsIgnoreCase("show tables")) {
            new ShowTable(this).processTokens();
        } else if (firstTwoWord.equalsIgnoreCase("delete from")) {
            new DeleteRow(this).processTokens();
        } else if (firstTwoWord.equalsIgnoreCase("alter table")) {
            new AlterTable(this).processTokens();
        } else if (firstTwoWord.equalsIgnoreCase("delete user")) {
            new DeleteUser(this).processTokens();
        } else if (firstTwoWord.equalsIgnoreCase("create database")) {
            new CreateDB(this).processTokens();
        } else if (firstTwoWord.equalsIgnoreCase("create user")) {
            new CreateUser(this).processTokens();
        } else if (firstTwoWord.equalsIgnoreCase("drop database")) {
            new DropDB(this).processTokens();
        } else if (firstWord.equalsIgnoreCase("truncate")) {
            new TruncateTable(this).processTokens();
        } else if (firstTwoWord.equalsIgnoreCase("show databases")) {
            new ShowDB(this).processTokens();
        } else if (firstWord.equalsIgnoreCase("use")) {
            new UseDB(this).processTokens();
        } else if (firstTwoWord.equalsIgnoreCase("start transaction")) {
            new StartTransaction(this).processTokens();
        } else if (firstTwoWord.equalsIgnoreCase("commit transaction")) {
            new CommitTrasaction(this).processTokens();
        } else if (firstTwoWord.equalsIgnoreCase("rollback transaction")) {
            new RollbackTransaction(this).processTokens();
        } else if (firstTwoWord.equalsIgnoreCase("create erd")) {
            new CreateErd(this).processTokens();
        } else if (firstTwoWord.equalsIgnoreCase("dump database")) {
            new DumpDB(this).processTokens();
        } else if (firstTwoWord.equalsIgnoreCase("source dump")) {
            new SourceDump(this).processTokens();
        } else {
            throw new RuntimeException("Invalid Syntax");
        }
    }

    private void validateSemicolonSuffix(String query) {
        if (query.startsWith("-u")) {
            return;
        }
        if (!query.endsWith(";")) {
            throw new RuntimeException("Invalid syntax(semi colon missing in the query)");
        }
    }

    public IInputs getInputs() {
        return inputs;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        String query = "update players set age=32 where name='Messi';";
        QueryProcessor a = new QueryProcessor(query);
        a.parseQuery();
    }
}
