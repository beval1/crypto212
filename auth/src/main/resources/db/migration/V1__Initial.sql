CREATE TABLE USERS (
    id BIGINT PRIMARY KEY NOT NULL,
    username VARCHAR(20) UNIQUE,
    email VARCHAR(45) UNIQUE,
    pass CHAR(60),
    account_enabled BOOLEAN NOT NULL,
    account_locked BOOLEAN NOT NULL,
    account_expired BOOLEAN NOT NULL,
    credentials_expired BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE ROLES (
    id BIGINT PRIMARY KEY NOT NULL,
    role_name VARCHAR(20) UNIQUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE USERS_ROLES (
    user_id BIGINT NOT NULL REFERENCES USERS(id),
    role_id BIGINT NOT NULL REFERENCES ROLES(id),
    PRIMARY KEY (user_id, role_id)
)

-- INSERT INTO USERS (id, username, password) VALUES (1, 'tutorialspoint.com');