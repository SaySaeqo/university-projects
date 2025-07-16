CREATE TRIGGER Stacje_delete 
	ON Stacje
	INSTEAD OF DELETE
AS
BEGIN
	/*SET NOCOUNT ON*/
	DELETE FROM Odcinki_tras
	WHERE pierwsza_stacja IN (SELECT deleted.nazwa_stacji FROM deleted);
	DELETE FROM Odcinki_tras
	WHERE druga_stacja IN (SELECT deleted.nazwa_stacji FROM deleted);

	DELETE FROM Odcinki_kursow 
	WHERE stacja IN (SELECT deleted.nazwa_stacji FROM deleted);
	DELETE FROM Odcinki_kursow 
	WHERE nastepna_stacja IN (SELECT deleted.nazwa_stacji FROM deleted);

	DELETE FROM Stacje
	WHERE nazwa_stacji IN (SELECT deleted.nazwa_stacji FROM deleted);

END