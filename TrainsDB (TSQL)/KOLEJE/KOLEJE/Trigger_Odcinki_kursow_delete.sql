CREATE TRIGGER Odcinki_kursow_delete 
	ON Odcinki_kursow
	INSTEAD OF DELETE
AS
BEGIN
	/*SET NOCOUNT ON*/
	DELETE FROM Wykupione_odcinki_kursow
	WHERE EXISTS (SELECT * FROM deleted WHERE
										nazwa_kursu = Wykupione_odcinki_kursow.nazwa_kursu
										AND stacja = Wykupione_odcinki_kursow.stacja)

	DELETE FROM Odcinki_kursow
	WHERE EXISTS (SELECT * FROM deleted WHERE
										nazwa_kursu = Odcinki_kursow.nazwa_kursu
										AND stacja = Odcinki_kursow.stacja)
END