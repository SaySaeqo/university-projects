USE KOLEJE_DB

-- proste z��czenie, by po��czy� unikatow� dla ka�dego biletu
-- nazw� kursu z kosztem biletu
SELECT w.nr_biletu, w.nazwa_kursu, b.koszt
INTO #tmp
	FROM Wykupione_odcinki_kursow w JOIN Bilety b
	ON w.nr_biletu = b.nr_biletu

-- zliczenie powt�rek (jeden bilet pozwala na podr�
-- po ca�ej d�ugo�ci kursu) i nadanie im l.p. (kt�r� powt�rk� jest)
SELECT *, ROW_NUMBER() OVER	(
	PARTITION BY
	nr_biletu,
	nazwa_kursu
	ORDER BY
	nr_biletu,
	nazwa_kursu
) row_num
INTO #zarobki_kursow
	FROM #tmp

DROP TABLE #tmp

-- usuniecie powt�rek
DELETE FROM #zarobki_kursow
WHERE row_num > 1

ALTER TABLE #zarobki_kursow
DROP COLUMN row_num

-- zsumowanie przychodu kurs�w z r�nych bilet�w
SELECT
nazwa_kursu, 
SUM(koszt) as laczny_przychod
FROM #zarobki_kursow
GROUP BY nazwa_kursu
ORDER BY laczny_przychod DESC

DROP TABLE #zarobki_kursow
