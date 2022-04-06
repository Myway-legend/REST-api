CREATE TABLE Subjects (
                          ID SERIAL NOT NULL PRIMARY KEY,
                          NAME VARCHAR(50) NOT NULL
);

CREATE TABLE Groups (
                        ID SERIAL NOT NULL PRIMARY KEY,
                        NAME VARCHAR(50) NOT NULL
);

CREATE TABLE People (
                        ID SERIAL NOT NULL PRIMARY KEY,
                        FIRST_NAME VARCHAR(20) NOT NULL,
                        LAST_NAME VARCHAR(20) NOT NULL,
                        PATHER_NAME VARCHAR(50),
                        GROUP_ID INTEGER NOT NULL,
                        TYPE CHAR(1) NOT NULL,
                        FOREIGN KEY (GROUP_ID) REFERENCES Groups(ID)
);

CREATE TABLE Marks (
                       ID SERIAL NOT NULL PRIMARY KEY,
                       STUDENT_ID INTEGER NOT NULL,
                       SUBJECT_ID INTEGER NOT NULL,
                       TEACHER_ID INTEGER NOT NULL,
                       VALUE INTEGER NOT NULL,
                       FOREIGN KEY (STUDENT_ID) REFERENCES People(ID),
                       FOREIGN KEY (SUBJECT_ID) REFERENCES Subjects(ID),
                       FOREIGN KEY (TEACHER_ID) REFERENCES People(ID)
);

INSERT INTO Groups (NAME) VALUES ('3530904/00001');
INSERT INTO Groups (NAME) VALUES ('3530904/00002');
INSERT INTO Groups (NAME) VALUES ('3530904/00003');
INSERT INTO Groups (NAME) VALUES ('3530904/00004');
INSERT INTO Groups (NAME) VALUES ('3530904/00005');

INSERT INTO Subjects (NAME) VALUES ('Programming');
INSERT INTO Subjects (NAME) VALUES ('Math');
INSERT INTO Subjects (NAME) VALUES ('English');

INSERT INTO People (FIRST_NAME, LAST_NAME, PATHER_NAME, GROUP_ID, TYPE) VALUES (
     'VASYA', 'PUPKIN', 'KONSTANTINOVICH', 1, 'P'
                                                                               );
INSERT INTO People (FIRST_NAME, LAST_NAME, PATHER_NAME, GROUP_ID, TYPE) VALUES (
     'RICK', 'GRIMES', NULL, 1, 'P'
                                                                               );
INSERT INTO People (FIRST_NAME, LAST_NAME, PATHER_NAME, GROUP_ID, TYPE) VALUES (
     'STEPAN', 'USHAKOV', 'GENADIEVICH', 1, 'S'
                                                                               );
INSERT INTO People (FIRST_NAME, LAST_NAME, PATHER_NAME, GROUP_ID, TYPE) VALUES (
    'KONSTANTINE', 'SMIRNOV', 'OLEGOVICH', 3, 'S'
                                                                               );

INSERT INTO Marks (STUDENT_ID, SUBJECT_ID, TEACHER_ID, VALUE) VALUES (
     2, 3, 1, 5
                                                                     );

INSERT INTO Marks (STUDENT_ID, SUBJECT_ID, TEACHER_ID, VALUE) VALUES (
     3, 3, 1, 3
                                                                     );

DROP TABLE Groups CASCADE;
DROP TABLE marks CASCADE;
DROP TABLE people CASCADE;
DROP TABLE subjects CASCADE;