# Trains Database

Study of TSQL database oparations using MS SQL Server Managment Studio. Detailed info about database's structure and relations can by found in `Tomasz_Piwowski_Baza_Danych.pdf`. All data is fake created by me for educational purposes.

## How to run and test

Run `.sql` files in following order:
1. CreateAllTables
2. (Unnecessary for queries starting with ZAD3) All Triggers (10)
3. InsertValues
4. ZAD3_Create_... (3)
5. ZAD3_... (Queries that are returning some results)
6. CascadeOperations (For modifying some data using cascade effect)
7. (For reseting) DeleteAllTables and go back to point 1 

## Used mechanics
- Creating, deleting and modyfing tables and rows
- Cascade operations
- Temporary tables
- Fields validations
- Tables relations using foreign keys
- Triggers
- Complex SELECT statements (having more than 1 SELECT keyword)
- Views
- Sql builtin functions like SUM, ROW and COUNT