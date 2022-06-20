-- wyswietla wszystkie informacje ³¹cznie z ³¹cznym czasem podró¿y
-- na dzieñ dla najbardziej esploatowanego pociagu 
-- (maj¹cego najd³u¿szy czas podró¿y na dzieñ)
CREATE VIEW wyswietl_najbardziej_esploatowany_pociag
AS 
SELECT TOP 1
p. nr_pociagu,
kategoria,
przewoznik,
liczba_miejsc_siedzacych,
liczba_wagonow,
laczny_czas_podrozy
FROM Pociagi_czas_podrozy c
JOIN Pociagi p
ON p.nr_pociagu = c.nr_pociagu
ORDER BY laczny_czas_podrozy DESC