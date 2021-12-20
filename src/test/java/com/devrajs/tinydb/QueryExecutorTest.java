package com.devrajs.tinydb;

import com.devrajs.tinydb.inputs.StoredInputs;
import com.devrajs.tinydb.queries.QueryExecutor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class QueryExecutorTest {
    static List<String> inputs;
    static StoredInputs storedInputs;
    static QueryExecutor queryExecutor;

    @BeforeAll
    public static void init() throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        inputs = new ArrayList<>();
        storedInputs = new StoredInputs();
        queryExecutor = new QueryExecutor(storedInputs);
        queryExecutor.init();
        inputProviders();
    }

    static Stream<String> inputProviders() throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        String epochTime = String.valueOf(System.currentTimeMillis());
        //StoredInputs inputs = new StoredInputs();
        inputs.add("-u root -p root123");
        inputs.add("create user with(NND_" + epochTime + ",abc123);");
        inputs.add("tommy123");
        inputs.add("create user with(NND_" + epochTime + ",abc123);"); // expected duplicate user error.
        inputs.add("show databases;");
        String dbName = epochTime;
        inputs.add(String.format("create database %s ;", dbName));
        inputs.add(String.format("create database %s ;", dbName)); // expected duplicate database error.
        inputs.add(String.format("use %s;", dbName));
        inputs.add("show databases;");
        inputs.add("create table players (name string, age integer, country string, height double, isChampion boolean);");
        inputs.add("create table players (name string, age integer);"); // duplicate tables
        inputs.add("create table balbla2(name varchar, age integer);"); // invalid datatype.
        inputs.add("show tables;");
        inputs.add("insert into players('Ronaldo', 30, 'Purtagal', 6.2, true);");
        inputs.add("insert into players('Villa', 40, 'Spain', 5.9, false);");
        inputs.add("insert into players('Messi', 28, 'Argentina', 5.8, true);");
        inputs.add("insert into players('Maradona', 50, 'Argentina', 5.6, true);");
        inputs.add("insert into players(Ronaldo, 30, 'Purtagal', 6.2, true);");
        inputs.add("insert into players('Ronaldo', abc, 'Purtagal', 6.2, true);");
        inputs.add("insert into players('Ronaldo', 30, 'Purtagal', 6.2, yes);");
        inputs.add("select * from players;");
        inputs.add("select * from players where name='Messi';");
        inputs.add("select * from players where age=40;");
        inputs.add("select * from players where isChampion=true;");
        inputs.add("select * from players where height=5.9;");
        inputs.add("select * from players where age > 35;");
        inputs.add("select * from players where age < 35;");
        inputs.add("select * from players where isChampion=true and age=30;");
        inputs.add("select * from players where isChampion=true or age=30;");
        inputs.add("start transaction;");
        inputs.add("insert into players('Blabala1', 30, 'Akha', 6.2, true);");
        inputs.add("insert into players('Glagla1', 40, 'Lakha', 5.9, false);");
        inputs.add("commit transaction;");
        inputs.add("select * from players;");
        inputs.add("start transaction;");
        inputs.add("insert into players('aborted_player', 30, 'aborted', 6.2, true);");
        inputs.add("rollback transaction;");
        inputs.add("select * from players;");
        inputs.add("update players set age=32 where name='Messi';");
        inputs.add("select * from players;");
        inputs.add("update players set age=60 where country='Argentina' and height=5.6;");
        inputs.add("select * from players;");
        inputs.add("alter table players add column weight double;");
        inputs.add("select * from players;");
        inputs.add("alter table players drop column weight;");
        inputs.add("select * from players;");
        // Primary key and Foreign Key testing:
        inputs.add("create table club (clubId integer, name string, origin string);");
        inputs.add("create table FootballClub (footballClubId integer primary key, name string, origin string, totalMembers integer);");
        inputs.add(
                "create table Footballer (footBallerId integer, name string, clubId integer references NoSuchTable(clubId));");
        inputs.add(
                "create table Footballer (footBallerId integer, name string, clubId integer references club(NoSuchClub));");
        inputs.add(
                "create table Footballer (footBallerId integer primary key, name string, clubId integer references club(clubId));");
        inputs.add(
                "create table Footballer (footBallerId integer primary key, name string, clubId integer references FootballClub(totalMembers));");
        inputs.add(
                "create table Footballer (footBallerId integer primary key, name string, clubId integer references FootballClub(footballClubId));");

        inputs.add("insert into FootballClub(1, 'MadridClub', 'Spain', 1000);");
        inputs.add("insert into Footballer(1, 'Nuemar', 1);");
        inputs.add("insert into Footballer(1, 'Nuemar', 1);"); // primary key violation
        inputs.add("insert into Footballer(2, 'Messi', 100);"); // foreign key violation
        inputs.add(String.format("dump database %s false;", dbName));
        inputs.add(String.format("dump database %s true;", dbName));
        String dumpRestoreDB = "dumpRestoreDB_" + epochTime;
        inputs.add(String.format("create database %s;", dumpRestoreDB));
        inputs.add(String.format("use %s;", dumpRestoreDB));
        // TODO Implement it.
        //String sqlDumpFile = getMostRecentFile();
        //inputs.add(String.format("source dump %s;", sqlDumpFile));
        inputs.add(String.format("use %s;", dbName));
        inputs.add(String.format("create erd %s;", dbName));
        inputs.add("q");
        return inputs.stream();
        //QueryExecutor queryExecutor = new QueryExecutor(inputs);
        //queryExecutor.startApplication();
    }

    @ParameterizedTest
    @MethodSource("inputProviders")
    public void testQueries(String input) {
        try {
            queryExecutor.processQuery(input);
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
