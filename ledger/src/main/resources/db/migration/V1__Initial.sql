CREATE TABLE transaction_type
(
    id BIGINT PRIMARY KEY,
    name VARCHAR(30)
);

CREATE TABLE transactions
(
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL ,
    wallet_id BIGINT NOT NULL ,
    transaction_type BIGINT REFERENCES transaction_type(id),
    transaction_amount DECIMAL(21,12) NOT NULL,
    asset_symbol VARCHAR(5) NOT NULL ,
    created_at TIMESTAMP NOT NULL
);