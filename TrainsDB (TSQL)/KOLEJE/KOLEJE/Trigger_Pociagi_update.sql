CREATE TRIGGER Pociagi_update
	ON Pociagi
	INSTEAD OF UPDATE
AS
BEGIN
	/*SET NOCOUNT ON*/

	INSERT INTO Pociagi
	SELECT * FROM inserted
	WHERE nr_pociagu NOT IN (SELECT nr_pociagu FROM Kursy)

/*------------------------*/

	UPDATE Kursy SET nr_pociagu = inserted.nr_pociagu FROM inserted
	WHERE Kursy.nr_pociagu IN (SELECT nr_pociagu from deleted)
		AND Kursy.nr_pociagu NOT IN (SELECT nr_pociagu from inserted)


/*------------------------*/

	UPDATE Pociagi
	SET
		kategoria = inserted.kategoria,
		przewoznik = inserted.przewoznik,
		liczba_miejsc_siedzacych = inserted.liczba_miejsc_siedzacych,
		liczba_wagonow = inserted.liczba_wagonow
	FROM inserted
	WHERE Pociagi.nr_pociagu IN (SELECT nr_pociagu FROM inserted)

	DELETE FROM Pociagi
	WHERE nr_pociagu IN (SELECT nr_pociagu FROM deleted)
		AND nr_pociagu NOT IN (SELECT nr_pociagu FROM inserted)
		

END