CREATE TABLE user (
    id                      INTEGER IDENTITY PRIMARY KEY,
    username                TEXT NOT NULL,
    password                TEXT NOT NULL,
    first_name              TEXT,
    last_name               TEXT,
    CONSTRAINT user_username_unique UNIQUE (username)
);

CREATE TABLE address (
    id                      INTEGER IDENTITY PRIMARY KEY,
    user_id                 INTEGER,
    street                  TEXT,
    state                   TEXT,
    zip                     TEXT,
    FOREIGN KEY(user_id) REFERENCES user(id)
);

INSERT INTO user VALUES(0,'administrator','ktAnvxiWdwjG884FbeCkr/rsPoOqkcNaR/kykh7y8whLNiRcKeu47ACNFnen0zMH','John', 'Doe');

INSERT INTO address VALUES(0,0,'234 Spruce Avenue Belleville','NJ','07109');
INSERT INTO address VALUES(1,0,'4031 Goodwin Avenue','WA','99205');
