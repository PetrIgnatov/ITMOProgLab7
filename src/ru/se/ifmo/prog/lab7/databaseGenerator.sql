BEGIN;

DROP TABLE IF EXISTS USERS CASCADE;
DROP TABLE IF EXISTS DRAGONS CASCADE;
DROP TYPE IF EXISTS COLOR CASCADE;
DROP TYPE IF EXISTS DRAGONTYPE CASCADE;
DROP TYPE IF EXISTS DRAGONCHARACTER CASCADE;

CREATE TYPE COLOR AS ENUM ('GREEN','YELLOW','ORANGE', 'WHITE');
CREATE TYPE DRAGONTYPE AS ENUM('WATER', 'UNDERGROUND', 'AIR');
CREATE TYPE DRAGONCHARACTER AS ENUM('EVIL', 'GOOD', 'CHAOTIC', 'CHAOTIC_EVIL', 'FICKLE');

CREATE TABLE IF NOT EXISTS USERS(
	name TEXT NOT NULL PRIMARY KEY,
	password TEXT NOT NULL
);

INSERT INTO USERS VALUES
('admin', 'admin');

CREATE TABLE IF NOT EXISTS DRAGONS(
	id SERIAL UNIQUE PRIMARY KEY,
	name TEXT NOT NULL CHECK (name != ''),
	x INTEGER NOT NULL CHECK (x>-32),
	y REAL NOT NULL,
	creationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	age INTEGER CHECK (age > 0),
	color COLOR,
	type DRAGONTYPE,
	character DRAGONCHARACTER,
	depth DOUBLE PRECISION NOT NULL,
	numberOfTreasures REAL CHECK(numberOfTreasures > 0),
	login TEXT NOT NULL
	
);

ALTER TABLE DRAGONS
ALTER COLUMN creationDate TYPE TIMESTAMP(0);
COMMIT;
