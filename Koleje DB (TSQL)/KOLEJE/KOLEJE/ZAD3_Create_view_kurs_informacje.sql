-- kilkukrotne z³¹czenie, które pokazuje informacje o kursie 
-- (dane o poci¹gu, stacje poœrednie, godziny przyjazdów i odjazdów)
-- dla ka¿dego odcinka trasy
CREATE VIEW pokaz_informacje_o_kursie
AS
SELECT k.*,
CAST(DATEADD(ms, DATEDIFF(ms, 0, k.godzina_odjazdu) + DATEDIFF(ms, 0, t.czas_podrozy), 0) AS TIME)  as godzina_przyjazdu,
t.dystans,
p.*
FROM Odcinki_kursow k
JOIN Odcinki_tras t
ON (k.stacja = t.pierwsza_stacja
AND k.nastepna_stacja = t.druga_stacja)
OR
(k.stacja = t.druga_stacja
AND k.nastepna_stacja = t.pierwsza_stacja)
JOIN Kursy ku
ON ku.nazwa_kursu = k.nazwa_kursu
JOIN Pociagi p
ON ku.nr_pociagu = p .nr_pociagu