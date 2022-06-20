CREATE TRIGGER Odcinki_tras_delete 
	ON Odcinki_tras
	INSTEAD OF DELETE
AS
BEGIN
	/*SET NOCOUNT ON*/
	DELETE FROM Wykupione_odcinki_tras
	WHERE EXISTS (SELECT * FROM deleted WHERE
										pierwsza_stacja = Wykupione_odcinki_tras.pierwsza_stacja
										AND druga_stacja = Wykupione_odcinki_tras.druga_stacja)

	DELETE FROM Odcinki_tras
	WHERE EXISTS (SELECT * FROM deleted WHERE
										pierwsza_stacja = Odcinki_tras.pierwsza_stacja
										AND druga_stacja = Odcinki_tras.druga_stacja)
END