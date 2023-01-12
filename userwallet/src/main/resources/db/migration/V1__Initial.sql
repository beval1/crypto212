CREATE TABLE WALLETS (
    id BIGINT PRIMARY KEY ,
    user_id BIGINT NOT NULL UNIQUE ,
    created_at TIMESTAMP NOT NULL ,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE ASSETS (
    id BIGINT PRIMARY KEY ,
    asset_symbol VARCHAR(5) UNIQUE ,
    asset_name VARCHAR(30) UNIQUE ,
    created_at TIMESTAMP NOT NULL ,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE WALLETS_ASSETS (
    wallet_id BIGINT REFERENCES WALLETS(id),
    asset_id BIGINT REFERENCES ASSETS(id),
    amount DECIMAL(21, 12) ,
    PRIMARY KEY (asset_id, wallet_id)
);

