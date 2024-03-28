-- Active: 1711128350343@@127.0.0.1@3306@balance

-- See User and Host --
SELECT User, Host FROM mysql.user;

-- Create Database name "Balance" --
CREATE DATABASE Balance;

-- Use Balance Database --
USE Balance;

-- Create Table --
-- Fields Explanation --
-- Date = Date report -- 
-- Code = Stock code --
-- IS = Insurance --
-- CP = Corporate --
-- PF = Pension Fund --
-- IB = Bank --
-- ID = Individual --
-- MF = Mutual Fund --
-- SC = Securities  --
-- FD = Foundation --
-- OT = Other --
CREATE TABLE Stocks(
	`Date` DATE,
    `Code` CHAR(4),
	`Local IS` BIGINT UNSIGNED,
	`Local CP` BIGINT UNSIGNED,
	`Local PF` BIGINT UNSIGNED,
	`Local IB` BIGINT UNSIGNED,
	`Local ID` BIGINT UNSIGNED,
	`Local MF` BIGINT UNSIGNED,
    `Local SC` BIGINT UNSIGNED,
	`Local FD` BIGINT UNSIGNED,
    `Local OT` BIGINT UNSIGNED,
    `Foreign IS` BIGINT UNSIGNED,
	`Foreign CP` BIGINT UNSIGNED,
	`Foreign PF` BIGINT UNSIGNED,
	`Foreign IB` BIGINT UNSIGNED,
	`Foreign ID` BIGINT UNSIGNED,
	`Foreign MF` BIGINT UNSIGNED,
	`Foreign SC` BIGINT UNSIGNED,
	`Foreign FD` BIGINT UNSIGNED,
	`Foreign OT` BIGINT UNSIGNED,
    PRIMARY KEY (`Date`, `Code`)
);

-- Testing query --
SELECT * FROM Stocks WHERE `Code` = 'BBCA' ORDER BY `Date`;

-- Check the Stocks Table --
DESC Stocks;

-- Show tables in Database --
SHOW TABLES;

-- Clear table data --
TRUNCATE Table Stocks;