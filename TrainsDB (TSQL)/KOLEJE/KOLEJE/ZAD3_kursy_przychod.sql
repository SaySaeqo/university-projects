USE KOLEJE_DB

-- proste z³¹czenie, by po³¹czyæ unikatow¹ dla ka¿dego biletu
-- nazwê kursu z kosztem biletu
SELECT w.nr_biletu, w.nazwa_kursu, b.koszt
INTO #tmp
	FROM Wykupione_odcinki_kursow w JOIN Bilety b
	ON w.nr_biletu = b.nr_biletu

-- zliczenie powtórek (jeden bilet pozwala na podró¿
-- po ca³ej d³ugoœci kursu) i nadanie im l.p. (któr¹ powtórk¹ jest)
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

-- usuniecie powtórek
DELETE FROM #zarobki_kursow
WHERE row_num > 1

ALTER TABLE #zarobki_kursow
DROP COLUMN row_num

-- zsumowanie przychodu kursów z ró¿nych biletów
SELECT
nazwa_kursu, 
SUM(koszt) as laczny_przychod
FROM #zarobki_kursow
GROUP BY nazwa_kursu
ORDER BY laczny_przychod DESC

DROP TABLE #zarobki_kursow
