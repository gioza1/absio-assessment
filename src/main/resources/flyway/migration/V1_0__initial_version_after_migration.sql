CREATE TABLE user (
    id                      INTEGER IDENTITY PRIMARY KEY,
    username                TEXT NOT NULL,
    password                TEXT NOT NULL,
    CONSTRAINT user_username_unique UNIQUE (username)
);

INSERT INTO user VALUES(0,'administrator','ktAnvxiWdwjG884FbeCkr/rsPoOqkcNaR/kykh7y8whLNiRcKeu47ACNFnen0zMH');