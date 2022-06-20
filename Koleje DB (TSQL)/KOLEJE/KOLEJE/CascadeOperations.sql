Use KOLEJE_DB
SELECT * FROM Wykupione_odcinki_tras

UPDATE Stacje SET nazwa_stacji = 'TEST3'
	WHERE nazwa_stacji = 'Bydgoszcz G³ówna'

SELECT * FROM Wykupione_odcinki_tras

/*-------------------*/

SELECT * FROM Bilety_jednorazowe

UPDATE Bilety SET nr_biletu = 'B123456'
	WHERE nr_biletu = 'B000001'

SELECT * FROM Bilety_jednorazowe

/*-------------------*/

SELECT * FROM Wykupione_odcinki_kursow

DELETE FROM Bilety WHERE data_zakupu > '2020-05-31'

SELECT * FROM Wykupione_odcinki_kursow

/*-------------------*/

SELECT * FROM Odcinki_kursow

DELETE FROM Stacje WHERE liczba_peronow < 4

SELECT * FROM Odcinki_kursow