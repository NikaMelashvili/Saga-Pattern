-- Create the database if it does not exist
CREATE DATABASE IF NOT EXISTS consistent_data;

-- Use the database
USE consistent_data;

-- Create user_info table
CREATE TABLE IF NOT EXISTS user_info (
    id INT PRIMARY KEY AUTO_INCREMENT,
    birth_date DATETIME(6),
    address VARCHAR(255)
    );

-- Create user table with a foreign key referencing user_info
CREATE TABLE IF NOT EXISTS user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    user_info_id INT,
    FOREIGN KEY (user_info_id) REFERENCES user_info(id) ON DELETE CASCADE
    );
