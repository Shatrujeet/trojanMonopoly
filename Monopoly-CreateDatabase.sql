
CREATE DATABASE Users;
USE Users;

CREATE TABLE Users (
	username varchar(50) not null,
    passw varchar(50) not null, 
    totalGames int(10) not null, 
    gamesWon int(10) not null
);

INSERT INTO Users (username, passw, totalGames, gamesWon) VALUES ('Guest', 'g', 0, 0);

