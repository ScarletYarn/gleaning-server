CREATE DATABASE IF NOT EXISTS glean;

USE glean;

DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS hosts;

CREATE TABLE books (
    title VARCHAR(200) PRIMARY KEY,
    content VARCHAR(10000)
);

CREATE TABLE hosts (
    hostName VARCHAR(30),
    location VARCHAR(100) PRIMARY KEY
);

INSERT INTO books VALUES 
('Hamlet', 'Awesom Book'),
('Pride And Prejudice', 'Great');

INSERT INTO hosts VALUES
('orca', '47.103.95.85'),
('beluga', '49.235.218.186')
