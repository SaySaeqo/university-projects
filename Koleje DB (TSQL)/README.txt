--- INSTRUKCJA UTWORZENIA BAZY DANYCH O KOLEJACH ---
by Tomasz Piwowski

Kolejność wykonywania zapytań w MS SQL Server Managment Studio:
1. CreateAllTables
2. (niewymagany do działania zapytań z przedrostkiem ZAD3) Wszystkie Triggery (10)
3. InsertValues
5. ZAD3_Create_... (3 zapytania z takim przedrostkiem w nazwie)
6. ZAD3_... (zapytania do zad. 3. zwracające wyniki)

4.(opcjonalny) DeleteAllTables i powrót do punktu 1. (w celu zresetowania danych w bazie do wartości początkowych, a więc spójnych) 