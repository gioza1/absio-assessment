CREATE TABLE address (
    id                      INTEGER IDENTITY PRIMARY KEY,
    user_id                 INTEGER,
    street                  TEXT NOT NULL,
    state                   TEXT NOT NULL,
    zip                     TEXT NOT NULL,
    FOREIGN KEY(user_id) REFERENCES user(id)
);

INSERT INTO address VALUES(0,1,'Dawis Rd','Talisay','6000');
INSERT INTO address VALUES(1,3,'Colon St','Cebu City','6000');
INSERT INTO address VALUES(2,3,'Lahug Rd','Talamban','6000');