/*

    Database initialization
    1. Run this SQL for create user and database
    2. To create a schema run gradle tasks: flywayBaseline flywayMigrate

 */

create role zombie login password 'zombie';
create database zombie owner zombie;
