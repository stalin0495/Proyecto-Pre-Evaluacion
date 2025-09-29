DROP DATABASE IF EXISTS pichincha_customers;

CREATE DATABASE pichincha_customers;

USE pichincha_customers;

DROP TABLE IF EXISTS pichincha_customers.customers;

DROP TABLE IF EXISTS pichincha_customers.persons;

CREATE TABLE pichincha_customers.persons (
    person_id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    gender VARCHAR(255) NOT NULL,
    age TINYINT(4) UNSIGNED DEFAULT NULL,
    identification VARCHAR(100) UNIQUE,
    address VARCHAR(255),
    phone VARCHAR(50)
);

CREATE TABLE pichincha_customers.customers (
    customer_id VARCHAR(36) UNIQUE,
    person_id VARCHAR(36) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    status CHAR(1),
    CONSTRAINT fk_persons FOREIGN KEY (person_id) REFERENCES pichincha_customers.persons(person_id) ON DELETE CASCADE
);

DROP DATABASE IF EXISTS pichincha_accounts;

CREATE DATABASE pichincha_accounts;

USE pichincha_accounts;

DROP TABLE IF EXISTS pichincha_accounts.transactions;

DROP TABLE IF EXISTS pichincha_accounts.accounts;

CREATE TABLE pichincha_accounts.accounts (
    account_id VARCHAR(36) PRIMARY KEY, 
    customer_id VARCHAR(36) NOT NULL,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    account_type VARCHAR(255) NOT NULL,
    initial_balance DECIMAL(18, 2) NOT NULL,
    status CHAR(1) NOT NULL
);

CREATE TABLE pichincha_accounts.transactions (
    transaction_id VARCHAR(36) PRIMARY KEY, 
    account_id VARCHAR(36) NOT NULL,
    date TIMESTAMP NOT NULL,
    transaction_type VARCHAR(255) NOT NULL,
    amount DECIMAL(18, 2) NOT NULL,
    balance DECIMAL(18, 2) NOT NULL,
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES pichincha_accounts.accounts(account_id)
);
