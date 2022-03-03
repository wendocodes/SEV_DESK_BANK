CREATE TABLE user (
  id BIGINT AUTO_INCREMENT NOT NULL,
   created_timestamp TIMESTAMP NULL,
   updated_timestamp TIMESTAMP NULL,
   customer_number VARCHAR(255) NULL,
   first_name VARCHAR(255) NULL,
   last_name VARCHAR(255) NULL,
   date_of_birth date NULL,
   gender CHAR(255) NULL,
   type VARCHAR(255) NULL,
   CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE user ADD CONSTRAINT uc_user_customer_number UNIQUE (customer_number);

CREATE TABLE account (
  id BIGINT AUTO_INCREMENT NOT NULL,
  account_number VARCHAR(255) NULL,
   created_timestamp TIMESTAMP NULL,
   updated_timestamp TIMESTAMP NULL,
   name VARCHAR(255) NULL,
   pin VARCHAR(255) NULL,
   type CHAR(255) NULL,
   customer_id BIGINT NOT NULL,
   balance_value DOUBLE NULL,
   balance_currency VARCHAR(255) NULL,
   overdraft_limit_value DOUBLE NULL,
   overdraft_limit_currency VARCHAR(255) NULL,
   status INT NULL,
   `description` VARCHAR(255) NULL,
   annual_interest_rate DOUBLE NOT NULL,
   CONSTRAINT pk_account PRIMARY KEY (id)
);

ALTER TABLE account ADD CONSTRAINT uc_account_number UNIQUE (account_number);
ALTER TABLE account ADD CONSTRAINT FK_ACCOUNT_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES user (id);

CREATE TABLE transaction (
  id BIGINT AUTO_INCREMENT NOT NULL,
   created_timestamp TIMESTAMP NULL,
   updated_timestamp TIMESTAMP NULL,
   type VARCHAR(255) NULL,
   category VARCHAR(255) NULL,
   merchant VARCHAR(255) NULL,
   location VARCHAR(255) NULL,
   reference_number VARCHAR(255) NULL,
   account_id BIGINT NOT NULL,
   transaction_value DOUBLE NULL,
   transaction_currency VARCHAR(255) NULL,
   status VARCHAR(255) NULL,
   to_account_number VARCHAR(255) NULL,
   transaction_mode_reference_number VARCHAR(255) NULL,
   transaction_mode VARCHAR(255) NULL,
    is_external_transfer BIT(1) NULL,
   CONSTRAINT pk_transaction PRIMARY KEY (id)
);

ALTER TABLE transaction ADD CONSTRAINT uc_transaction_reference_number UNIQUE (reference_number);

ALTER TABLE transaction ADD CONSTRAINT FK_TRANSACTION_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES account (id);

CREATE TABLE card (
  id BIGINT AUTO_INCREMENT NOT NULL,
   created_timestamp TIMESTAMP NULL,
   updated_timestamp TIMESTAMP NULL,
   card_number VARCHAR(255) NULL,
   pin VARCHAR(255) NULL,
   type VARCHAR(255) NULL,
   account_id BIGINT NOT NULL,
   withdrawal_limit_value DOUBLE NULL,
   withdrawal_limit_currency VARCHAR(255) NULL,
   status INT NULL,
   `description` VARCHAR(255) NULL,
   CONSTRAINT pk_card PRIMARY KEY (id)
);

ALTER TABLE card ADD CONSTRAINT uc_card_account UNIQUE (account_id);

ALTER TABLE card ADD CONSTRAINT FK_CARD_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES account (id);