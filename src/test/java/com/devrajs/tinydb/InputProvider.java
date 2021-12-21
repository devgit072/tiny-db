package com.devrajs.tinydb;

import com.devrajs.tinydb.exception.QueryErrorException;
import com.devrajs.tinydb.exception.QuerySyntaxException;
import com.devrajs.tinydb.inputs.StoredInputs;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class InputProvider {
    static String database;

    public static void setDatabase(String databaseName) {
        database = databaseName;
    }

    public static Stream<TestInput> getInputsForTablesTest() {
        List<TestInput> inputs = new ArrayList<>();

        inputs.add(
                new TestInput("CREATE TABLE players (name string, age integer, country string, height double, isChampion boolean);"
                        , null));

        // Duplicate table creation attempt.
        inputs.add(
                new TestInput("CREATE TABLE players (name string, age integer);"
                        , new ExpectedException(new QueryErrorException(), "")));

        // Invalid datatype of column.
        inputs.add(
                new TestInput("create table players(name varchar, age integer);"
                        , new ExpectedException(new QuerySyntaxException(), "")));

        inputs.add(
                new TestInput("show tables;",
                        null));

        inputs.add(
                new TestInput("insert into players('Ronaldo', 30, 'Purtagal', 6.2, true);"
                        , null));
        inputs.add(
                new TestInput("insert into players('Villa', 40, 'Spain', 5.9, false);"
                        , null));
        inputs.add(
                new TestInput("insert into players('Messi', 28, 'Argentina', 5.8, true);"
                        , null));
        inputs.add(
                new TestInput("insert into players('Maradona', 50, 'Argentina', 5.6, true);"
                        , null));
        inputs.add(
                new TestInput("insert into players(Ronaldo, 30, 'Purtagal', 6.2, true);"
                        , new ExpectedException(new QuerySyntaxException(), null)));
        inputs.add(
                new TestInput("insert into players('Ronaldo', abc, 'Purtagal', 6.2, true);"
                        , new ExpectedException(new QuerySyntaxException(), null)));
        inputs.add(
                new TestInput("insert into players('Ronaldo', 30, 'Purtagal', 6.2, yes);"
                        , new ExpectedException(new QuerySyntaxException(), null)));
        inputs.add(
                new TestInput("select * from players;"
                        , null));
        inputs.add(
                new TestInput("select * from players where name='Messi';"
                        , null));
        inputs.add(new TestInput("select * from players where age=40;"
                , null));
        inputs.add(new TestInput("select * from players where isChampion=true;"
                , null));
        inputs.add(new TestInput("select * from players where height=5.9;"
                , null));
        inputs.add(new TestInput("select * from players where age > 35;"
                , null));
        inputs.add(
                new TestInput("select * from players where age < 35;", null));
        inputs.add(new TestInput("select * from players where isChampion=true and age=30;",
                null));

        inputs.add(new TestInput("select * from players where isChampion=true or age=30;"
                , null));
        inputs.add(new TestInput("update players set age=32 where name='Messi';", null));

        inputs.add(new TestInput("update players set age=60 where country='Argentina' and height=5.6;", null));

        inputs.add(new TestInput("alter table players add column weight double;", null));

        inputs.add(new TestInput("alter table players drop column weight;", null));

        inputs.add(new TestInput("create table club (clubId integer, name string, origin string);", null));

        inputs.add(new TestInput("create table FootballClub (footballClubId integer primary key, name string, origin string, totalMembers integer);", null));

        inputs.add(new TestInput("create table Footballer (footBallerId integer, name string, clubId integer references NoSuchTable(clubId));",
                new ExpectedException(new QueryErrorException(), null)));

        inputs.add(new TestInput("create table Footballer (footBallerId integer, name string, clubId integer references club(NoSuchClub));",
                new ExpectedException(new QueryErrorException(), null)));

        inputs.add(new TestInput("create table Footballer (footBallerId integer primary key, name string, clubId integer references club(clubId));",
                new ExpectedException(new QueryErrorException(), null)));

        inputs.add(new TestInput("create table Footballer (footBallerId integer primary key, name string, clubId integer references FootballClub(totalMembers));",
                new ExpectedException(new QueryErrorException(), null)));

        inputs.add(new TestInput("create table Footballer (footBallerId integer primary key, name string, clubId integer references FootballClub(footballClubId));", null));

        inputs.add(new TestInput("insert into FootballClub(1, 'MadridClub', 'Spain', 1000);", null));

        inputs.add(new TestInput("insert into Footballer(1, 'Nuemar', 1);", null));

        // primary key violation
        inputs.add(new TestInput("insert into Footballer(1, 'Nuemar', 1);",
                new ExpectedException(new QueryErrorException(), null)));

        // Foreign key violations.
        inputs.add(new TestInput("insert into Footballer(2, 'Messi', 100);",
                new ExpectedException(new QueryErrorException(), null)));

        return inputs.stream();
    }

    public static StoredInputs transactionQueries(String database) {
        StoredInputs storedInputs = new StoredInputs();
        storedInputs.add(String.format("use %s;", database));
        storedInputs.add("start transaction;");
        storedInputs.add("insert into players('Blabala1', 30, 'Akha', 6.2, true);");
        storedInputs.add("insert into players('Glagla1', 40, 'Lakha', 5.9, false);");
        storedInputs.add("commit transaction;");
        storedInputs.add("select * from players;");
        storedInputs.add("start transaction;");
        storedInputs.add("insert into players('aborted_player', 30, 'aborted', 6.2, true);");
        storedInputs.add("rollback transaction;");
        storedInputs.add("select * from players;");
        storedInputs.add("q");
        return storedInputs;
    }

    public static StoredInputs dumpQueries(String databaseName) {
        StoredInputs storedInputs = new StoredInputs();
        storedInputs.add(String.format("dump database %s false;", databaseName));
        storedInputs.add(String.format("dump database %s true SQLDump/%s_withData.sql;", databaseName, databaseName));
        storedInputs.add("q");
        return storedInputs;
    }
}
