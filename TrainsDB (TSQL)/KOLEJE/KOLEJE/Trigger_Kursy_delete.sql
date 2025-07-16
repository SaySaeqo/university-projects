CREATE TRIGGER Kursy_delete 
	ON Kursy
	INSTEAD OF DELETE
AS
BEGIN
	/*SET NOCOUNT ON*/
	DELETE FROM Odcinki_kursow
	WHERE nazwa_kursu IN (SELECT deleted.nazwa_kursu FROM deleted)

	DELETE FROM Kursy
	WHERE nazwa_kursu IN (SELECT deleted.nazwa_kursu FROM deleted)

END