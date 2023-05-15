#pragma once
#include "Swiat.h"
#include <string>
#define KIERUNKI 8
enum kierunek_t {
	LEWO, PRAWO, GORA, DOL, LEWO_GORA, LEWO_DOL, PRAWO_GORA, PRAWO_DOL
};

#define GATUNKI 10
enum gatunki_t {
	ANTYLOPA, BARSZCZ_SOSNOWSKIEGO, GUARANA, LIS, MLECZ, OWCA, TRAWA,
	WILCZE_JAGODY, WILK, ZOLW
};

class Organizm
{
private:
	Swiat* swiat;

protected:
	size_t inicjatywa;
	size_t wiek;
	size_t sila;
	wspolrzedne_t polozenie;
	bool czy_zyje;

public:
	Organizm(Swiat&, const size_t inicjatywa, const size_t sila, const wspolrzedne_t);
	Organizm(const Organizm&);

	wspolrzedne_t getPolozenie() const;
	size_t getInicjatywa() const;
	size_t getSila() const;
	Swiat& getSwiat() const;
	bool czyUmarl() const;
	void setPolozenie(wspolrzedne_t);
	void setSila(size_t);

	virtual Organizm* stworzPotomka() = 0;
	virtual wspolrzedne_t akcja(const event_t);
	virtual bool przedAtakiem(Organizm& ofiara);
	virtual void poAtaku();
	virtual void obrona(Organizm& napastnik);
	virtual void rysuj();
	virtual void postarz();
	virtual void smierc();
	virtual std::string toString() const;

	static int compareOrganizm(const void*, const void*);
	wspolrzedne_t pole(kierunek_t) const;
	bool szukajWolnegoPola() const;

};

