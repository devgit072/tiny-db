create table players(name string,age integer,country string,height double,isChampion boolean);
create table club(clubId integer,name string,origin string);
create table FootballClub(footballClubId integer primary key,name string,origin string,totalMembers integer);
create table Footballer(footBallerId integer primary key,name string,clubId integer references FootballClub(footballClubId));

insert into players('Ronaldo',30,'Purtagal',6.2,true);
insert into players('Villa',40,'Spain',5.9,false);
insert into players('Messi',32,'Argentina',5.8,true);
insert into players('Maradona',60,'Argentina',5.6,true);
insert into players('Blabala1',30,'Akha',6.2,true);
insert into players('Glagla1',40,'Lakha',5.9,false);


insert into FootballClub(1,'MadridClub','Spain',1000);

insert into Footballer(1,'Nuemar',1);

