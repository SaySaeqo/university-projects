CREATE TRIGGER Stacje_update
	ON Stacje
	INSTEAD OF UPDATE
AS
BEGIN
	/*SET NOCOUNT ON*/

	INSERT INTO Stacje
	SELECT * FROM inserted
	WHERE nazwa_stacji NOT IN (SELECT nazwa_stacji FROM Stacje)

/*------------------------*/

	UPDATE Odcinki_tras SET pierwsza_stacja = inserted.nazwa_stacji FROM inserted
	WHERE Odcinki_tras.pierwsza_stacja IN (SELECT nazwa_stacji from deleted)
		AND Odcinki_tras.pierwsza_stacja NOT IN (SELECT nazwa_stacji from inserted)
	UPDATE Odcinki_tras SET druga_stacja = inserted.nazwa_stacji FROM inserted
	WHERE Odcinki_tras.druga_stacja IN (SELECT nazwa_stacji from deleted)
		AND Odcinki_tras.druga_stacja NOT IN (SELECT nazwa_stacji from inserted)

	UPDATE Odcinki_kursow SET stacja = inserted.nazwa_stacji FROM inserted
	WHERE Odcinki_kursow.stacja IN (SELECT nazwa_stacji from deleted)
		AND Odcinki_kursow.stacja NOT IN (SELECT nazwa_stacji from inserted)
	UPDATE Odcinki_kursow SET nastepna_stacja = inserted.nazwa_stacji FROM inserted
	WHERE Odcinki_kursow.nastepna_stacja IN (SELECT nazwa_stacji from deleted)
		AND Odcinki_kursow.nastepna_stacja NOT IN (SELECT nazwa_stacji from inserted)


/*------------------------*/

	UPDATE Stacje
	SET
		miasto = inserted.miasto,
		liczba_peronow = inserted.liczba_peronow
	FROM inserted
	WHERE Stacje.nazwa_stacji IN (SELECT nazwa_stacji FROM deleted)

	DELETE FROM Stacje
	WHERE nazwa_stacji IN (SELECT nazwa_stacji FROM deleted)
		AND nazwa_stacji NOT IN (SELECT nazwa_stacji FROM inserted)
		

END