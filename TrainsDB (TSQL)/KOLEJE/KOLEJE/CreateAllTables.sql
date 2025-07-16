Use KOLEJE_DB
CREATE TABLE Bilety(
	nr_biletu CHAR(7) PRIMARY KEY 
		CHECK(nr_biletu LIKE 'B[0-9][0-9][0-9][0-9][0-9][0-9]'),
	data_zakupu DATETIME,
	koszt MONEY,
	rodzaj_ulgi VARCHAR(20)
)
CREATE TABLE Bilety_jednorazowe(
	nr_biletu CHAR(7) PRIMARY KEY 
		REFERENCES Bilety
		ON UPDATE CASCADE ON DELETE CASCADE,
	data_odjazdu DATE,
	nr_wagonu INT CHECK(nr_wagonu > 0),
	nr_miejsca INT CHECK(nr_miejsca > 0 ),
	rodzaj_miejsca VARCHAR(10)
		CHECK(rodzaj_miejsca IN('Okno','Œrodek','Korytarz','Stoj¹ce'))
)
CREATE TABLE Klienci(
	nr_biletu CHAR(7) PRIMARY KEY 
		REFERENCES Bilety
		ON UPDATE CASCADE ON DELETE CASCADE,
	imie VARCHAR(20),
	nazwisko VARCHAR(20)
)
CREATE TABLE Bilety_okresowe(
	nr_biletu CHAR(7) PRIMARY KEY 
		REFERENCES Bilety
		ON UPDATE CASCADE ON DELETE CASCADE,
	data_rozpoczecia DATE,
	data_zakonczenia DATE,
	rodzaj_oferty VARCHAR(20)
)
CREATE TABLE Pociagi(
	nr_pociagu CHAR(4) PRIMARY KEY 
		CHECK(nr_pociagu LIKE 'P[0-9][0-9][0-9]'),
	kategoria VARCHAR(10),
	przewoznik VARCHAR(10),
	liczba_miejsc_siedzacych INT CHECK(liczba_miejsc_siedzacych > 0),
	liczba_wagonow INT CHECK(liczba_wagonow > 0)
)
CREATE TABLE Kursy(
	nazwa_kursu VARCHAR(10) PRIMARY KEY,
	nr_wagonu_na_poczatku_skladu INT CHECK (nr_wagonu_na_poczatku_skladu > 0),
	nr_wagonu_na_koncu_skladu INT CHECK (nr_wagonu_na_koncu_skladu > 0),
	nr_pociagu CHAR(4)
		REFERENCES Pociagi
)
CREATE TABLE Maszynisci(
	nr_pracownika CHAR(5) PRIMARY KEY 
		CHECK(nr_pracownika LIKE 'M[0-9][0-9][0-9][0-9]'),
	imie VARCHAR(20) NOT NULL,
	nazwisko VARCHAR(20) NOT NULL,
	nazwa_kursu VARCHAR(10) 
		REFERENCES Kursy
		ON UPDATE CASCADE ON DELETE CASCADE
)
CREATE TABLE Stacje(
	nazwa_stacji VARCHAR(20) PRIMARY KEY,
	miasto VARCHAR(20) NOT NULL,
	liczba_peronow TINYINT CHECK (liczba_peronow > 0)
)
CREATE TABLE Odcinki_tras(
	pierwsza_stacja VARCHAR(20) 
		REFERENCES Stacje,
	druga_stacja VARCHAR(20) 
		REFERENCES Stacje,
	dystans DECIMAL CHECK(dystans > 0),
	czas_podrozy TIME,
		PRIMARY KEY (pierwsza_stacja,druga_stacja)
)
CREATE TABLE Odcinki_kursow(
	nazwa_kursu VARCHAR(10) 
		REFERENCES Kursy,
	stacja VARCHAR(20) 
		REFERENCES Stacje,
	nastepna_stacja VARCHAR(20) 
		REFERENCES Stacje,
	godzina_odjazdu TIME NOT NULL,
		PRIMARY KEY (nazwa_kursu, stacja),
)
CREATE TABLE Wykupione_odcinki_kursow(
	nazwa_kursu VARCHAR(10),
	stacja VARCHAR(20),
	nr_biletu CHAR(7)
		REFERENCES Bilety_jednorazowe
		ON UPDATE CASCADE ON DELETE CASCADE,
		PRIMARY KEY (nazwa_kursu,stacja,nr_biletu),
		CONSTRAINT klucz_odcinka_kursu FOREIGN KEY
		(nazwa_kursu, stacja)
			REFERENCES Odcinki_kursow (nazwa_kursu, stacja)
)
CREATE TABLE Wykupione_odcinki_tras(
	pierwsza_stacja VARCHAR(20),
	druga_stacja VARCHAR(20),
	nr_biletu CHAR(7) 
		REFERENCES Bilety_okresowe
		ON UPDATE CASCADE ON DELETE CASCADE,
		PRIMARY KEY (pierwsza_stacja,druga_stacja,nr_biletu),
		CONSTRAINT klucz_odcnika_trasy FOREIGN KEY
		(pierwsza_stacja,druga_stacja) 
			REFERENCES Odcinki_tras (pierwsza_stacja,druga_stacja)
)