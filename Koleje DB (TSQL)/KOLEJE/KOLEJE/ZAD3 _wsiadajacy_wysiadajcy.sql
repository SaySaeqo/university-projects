Use KOLEJE_DB

-- proste z³¹czenie dwóch tablic, by móc póŸniej wydobyæ informacjê o tym,
-- która stacja dla danego biletu by³a t¹ ostatni¹/pierwsz¹
SELECT w.nr_biletu, o.nazwa_kursu, o.stacja, o.nastepna_stacja
INTO #tmp
	FROM Wykupione_odcinki_kursow w JOIN Odcinki_kursow o
	ON w.nazwa_kursu = o.nazwa_kursu 
	AND w.stacja = o.stacja

-- wyszukanie stacji ostatniej, sprawdzaj¹c czy istnieje 
-- stacja nastêpna dla niej, i policzenie liczby jej wyst¹pieñ
SELECT
	nastepna_stacja as stacja, 
	COUNT(nr_biletu) as osoby_wysiadajace
INTO #wysiadajacy
	FROM #tmp t
	WHERE NOT EXISTS (SELECT * 
						FROM #tmp u
						WHERE t.nr_biletu = u.nr_biletu
						AND t.nazwa_kursu = u.nazwa_kursu
						AND t.nastepna_stacja = u.stacja)
	GROUP BY nastepna_stacja

-- wyszukanie stacji pierwszej, sprawdzaj¹c czy istnieje stacja
-- poprzednia dla niej, i policzenie liczby jej wyst¹pieñ
SELECT
	nastepna_stacja as stacja, 
	COUNT(nr_biletu) as osoby_wsiadajace
INTO #wsiadajacy
	FROM #tmp t
	WHERE NOT EXISTS (SELECT * 
						FROM #tmp u
						WHERE t.nr_biletu = u.nr_biletu
						AND t.nazwa_kursu = u.nazwa_kursu
						AND t.stacja = u.nastepna_stacja)
	GROUP BY nastepna_stacja
	
-- wyœwietlenie wyników
SELECT * FROM #wsiadajacy ORDER BY osoby_wsiadajace DESC
SELECT * FROM #wysiadajacy ORDER BY osoby_wysiadajace DESC


DROP TABLE #wsiadajacy
DROP TABLE #wysiadajacy
DROP TABLE #tmp