CREATE TRIGGER Odcinki_tras_update
	ON Odcinki_tras
	INSTEAD OF UPDATE
AS
BEGIN
	/*SET NOCOUNT ON*/

	INSERT INTO Odcinki_tras
	SELECT * FROM inserted
	WHERE NOT EXISTS (SELECT * FROM Odcinki_tras WHERE
										pierwsza_stacja = inserted.pierwsza_stacja
										AND druga_stacja = inserted.druga_stacja)

/*------------------------*/

	UPDATE Wykupione_odcinki_tras
	SET pierwsza_stacja = inserted.pierwsza_stacja
	FROM inserted
	WHERE Wykupione_odcinki_tras.pierwsza_stacja IN (SELECT pierwsza_stacja FROM deleted)
		AND Wykupione_odcinki_tras.pierwsza_stacja NOT IN (SELECT pierwsza_stacja FROM inserted)
	UPDATE Wykupione_odcinki_tras
	SET druga_stacja = inserted.druga_stacja
	FROM inserted
	WHERE Wykupione_odcinki_tras.druga_stacja IN (SELECT druga_stacja FROM deleted)
		AND Wykupione_odcinki_tras.druga_stacja NOT IN (SELECT druga_stacja FROM inserted)


/*------------------------*/

	UPDATE Odcinki_tras
	SET
		dystans = inserted.dystans,
		czas_podrozy = inserted.czas_podrozy
	FROM inserted
	WHERE EXISTS (SELECT * FROM inserted WHERE
										pierwsza_stacja = Odcinki_tras.pierwsza_stacja
										AND druga_stacja = Odcinki_tras.druga_stacja)

	DELETE FROM Odcinki_tras
	WHERE EXISTS (SELECT * FROM deleted WHERE
										pierwsza_stacja = Odcinki_tras.pierwsza_stacja
										AND druga_stacja = Odcinki_tras.druga_stacja)
		AND NOT EXISTS (SELECT * FROM inserted WHERE
										pierwsza_stacja = Odcinki_tras.pierwsza_stacja
										AND druga_stacja = Odcinki_tras.druga_stacja)

END