USE KOLEJE_DB

-- po��czenie identyfikatora nr_biletu, kt�ry jednoznacznie okre�la
-- pasa�era z imieniem i nazwiskiem maszynisty, kt�ry prowadzi�
-- poci�g podczas jego podr�y
SELECT imie as imie_m, nazwisko as nazwisko_m, nr_biletu
INTO #tmp
FROM Wykupione_odcinki_kursow w JOIN Maszynisci m
ON w.nazwa_kursu = m.nazwa_kursu

-- ponumerowanie wierszy ze wzgl�du na l.p. powt�rki id nr_biletu
SELECT *, ROW_NUMBER() OVER	(
	PARTITION BY
	nr_biletu
	ORDER BY
	nr_biletu
) row_num
INTO #tmp2
	FROM #tmp

DROP TABLE #tmp

-- usuni�cie powt�rek
DELETE FROM #tmp2
WHERE row_num > 1

ALTER TABLE #tmp2
DROP COLUMN row_num

-- wydobycie informacji o imieniu, nazwisku pasa�era i dacie jego podr�y
SELECT imie_m, nazwisko_m, 
imie as imie_p, nazwisko as nazwisko_p,
nr_wagonu, data_odjazdu
INTO #maszynisci_z_pasazerami
FROM #tmp2 m 
JOIN Klienci k
ON m.nr_biletu = k.nr_biletu
JOIN Bilety_jednorazowe b
ON m.nr_biletu = b.nr_biletu

DROP TABLE #tmp2

-- sprawdzenie, czy para maszynisty i pasa�era dzieli�a 
-- ten sam poci�g w danym dniu
-- (mo�e by� przydatne na wezwanie policji, celem jakiego� �ledztwa)
SELECT * FROM #maszynisci_z_pasazerami
WHERE imie_m = 'Piotr'
AND nazwisko_m = 'Nowak'
AND imie_p = 'Jan'
AND nazwisko_p = 'Pozorski'
AND data_odjazdu = '2020-01-01'


DROP TABLE #maszynisci_z_pasazerami