create database if not exists chatting;
use chatting;
drop table if exists user;
create table user(
id int primary key,
acnum int,
nickname varchar(100),
sex varchar(45),
age int, 
password varchar(20),
signature varchar(100),
email varchar(50),
photo int,
state int,
listFriend varchar(100),
registerTime datetime) 
CHARACTER SET utf8 COLLATE utf8_general_ci;