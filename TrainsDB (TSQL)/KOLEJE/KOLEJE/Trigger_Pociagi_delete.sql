CREATE TRIGGER Pociagi_delete
	ON Pociagi
	INSTEAD OF DELETE
AS
BEGIN
	/*SET NOCOUNT ON*/
	DELETE FROM Kursy
	WHERE nr_pociagu IN (SELECT deleted.nr_pociagu FROM deleted)

	DELETE FROM Pociagi
	WHERE nr_pociagu IN (SELECT deleted.nr_pociagu FROM deleted)

END