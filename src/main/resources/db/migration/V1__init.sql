DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS chats;

CREATE TYPE message_type AS ENUM ('WELCOME', 'SCHEDULED');
CREATE TYPE message_status AS ENUM ('AWAIT', 'DONE');

CREATE TABLE messages
(
    id             SERIAL PRIMARY KEY,
    chat_id        BIGINT NOT NULL,
    type           TEXT   NOT NULL,
    text           TEXT   NOT NULL,
    scheduled_time TIMESTAMP(0),
    status         TEXT
);

CREATE TABLE chats
(
    chat_id BIGINT       NOT NULL PRIMARY KEY,
    name    VARCHAR(255) NOT NULL
);



