CREATE TRIGGER Kursy_update
	ON Kursy
	INSTEAD OF UPDATE
AS
BEGIN
	/*SET NOCOUNT ON*/

	INSERT INTO Kursy
	SELECT * FROM inserted
	WHERE nazwa_kursu NOT IN (SELECT nazwa_kursu FROM Kursy)

/*------------------------*/

	UPDATE Odcinki_kursow SET nazwa_kursu = inserted.nazwa_kursu FROM inserted
	WHERE Odcinki_kursow.nazwa_kursu IN (SELECT nazwa_kursu from deleted)
		AND Odcinki_kursow.nazwa_kursu NOT IN (SELECT nazwa_kursu from inserted)


/*------------------------*/

	UPDATE Kursy
	SET
		nr_wagonu_na_poczatku_skladu = inserted.nr_wagonu_na_poczatku_skladu,
		nr_wagonu_na_koncu_skladu = inserted.nr_wagonu_na_koncu_skladu,
		nr_pociagu = inserted.nr_pociagu
	FROM inserted
	WHERE Kursy.nazwa_kursu IN (SELECT nazwa_kursu FROM inserted)

	DELETE FROM Kursy
	WHERE nazwa_kursu IN (SELECT nazwa_kursu FROM deleted)
		AND nazwa_kursu NOT IN (SELECT nazwa_kursu FROM inserted)
		

END