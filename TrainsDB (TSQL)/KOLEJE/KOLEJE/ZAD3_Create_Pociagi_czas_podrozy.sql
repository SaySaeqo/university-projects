USE KOLEJE_DB

-- wydobycie informacji o czasie podró¿y dla danych fragmentów
-- kursu z jednoczesnym ich zsumowaniem dla ca³ego kursu
SELECT k.nazwa_kursu, 
DATEADD(ms, SUM(DATEDIFF(ms, 0, czas_podrozy)), 0) as laczny_czas_podrozy
INTO #tmp
FROM Odcinki_kursow k
JOIN Odcinki_tras t
ON (k.stacja = t.pierwsza_stacja
AND k.nastepna_stacja = t.druga_stacja)
OR
(k.stacja = t.druga_stacja
AND k.nastepna_stacja = t.pierwsza_stacja)
GROUP BY k.nazwa_kursu

-- stworzenia tabeli, by zachowaæ typ TIME dla atrybuty laczny_czas_podrozy
CREATE TABLE Pociagi_czas_podrozy(
nr_pociagu CHAR(4) PRIMARY KEY 
		CHECK(nr_pociagu LIKE 'P[0-9][0-9][0-9]'),
laczny_czas_podrozy TIME
)

-- jeden poci¹g mo¿e mieæ przypisanych kilka kursów, 
-- wiêc w tym kroku sumujemy powtórki dla ka¿dego takiego przypadku
INSERT INTO Pociagi_czas_podrozy
SELECT p.nr_pociagu,
DATEADD(ms, SUM(DATEDIFF(ms, 0, laczny_czas_podrozy)), 0) as laczny_czas_podrozy
FROM #tmp t JOIN Kursy k
ON t.nazwa_kursu = k.nazwa_kursu
RIGHT JOIN Pociagi p
ON p.nr_pociagu = k.nr_pociagu
GROUP BY p.nr_pociagu

DROP TABLE #tmp