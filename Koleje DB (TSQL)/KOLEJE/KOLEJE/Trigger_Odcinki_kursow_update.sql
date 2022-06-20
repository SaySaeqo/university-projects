CREATE TRIGGER Odcinki_kursow_update
	ON Odcinki_kursow
	INSTEAD OF UPDATE
AS
BEGIN
	/*SET NOCOUNT ON*/

	INSERT INTO Odcinki_kursow
	SELECT * FROM inserted
	WHERE NOT EXISTS (SELECT * FROM Odcinki_kursow WHERE
										nazwa_kursu = inserted.nazwa_kursu
										AND stacja = inserted.stacja)

/*------------------------*/
	
	UPDATE Wykupione_odcinki_kursow 
	SET nazwa_kursu = inserted.nazwa_kursu
	FROM inserted
	WHERE Wykupione_odcinki_kursow.nazwa_kursu IN (SELECT nazwa_kursu FROM deleted)
		AND Wykupione_odcinki_kursow.nazwa_kursu NOT IN (SELECT nazwa_kursu FROM inserted)
	UPDATE Wykupione_odcinki_kursow 
	SET stacja = inserted.stacja
	FROM inserted
	WHERE Wykupione_odcinki_kursow.stacja IN (SELECT stacja FROM deleted)
		AND Wykupione_odcinki_kursow.stacja NOT IN (SELECT stacja FROM inserted)


/*------------------------*/

	UPDATE Odcinki_kursow
	SET
		nastepna_stacja = inserted.nastepna_stacja,
		godzina_odjazdu = inserted.godzina_odjazdu
	FROM inserted
	WHERE EXISTS (SELECT * FROM inserted WHERE
										nazwa_kursu = Odcinki_kursow.nazwa_kursu
										AND stacja = Odcinki_kursow.stacja)
		
	DELETE FROM Odcinki_kursow
	WHERE EXISTS (SELECT * FROM deleted WHERE
										nazwa_kursu = Odcinki_kursow.nazwa_kursu
										AND stacja = Odcinki_kursow.stacja)
		AND NOT EXISTS (SELECT * FROM inserted WHERE
										nazwa_kursu = Odcinki_kursow.nazwa_kursu
										AND stacja = Odcinki_kursow.stacja)

END