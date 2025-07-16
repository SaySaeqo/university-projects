#include "Roslina.h"
#include <iostream>
#define JAK_CZESTO_ROZSIEWA 30


Roslina::Roslina(Swiat& swiat, const size_t sila, const wspolrzedne_t polozenie) :
	Organizm(swiat, 0, sila, polozenie) { }
Roslina::Roslina(const Roslina& org) :
	Organizm(org) { }


wspolrzedne_t Roslina::akcja(const event_t event) {
	if (szukajWolnegoPola() && rand() % JAK_CZESTO_ROZSIEWA == 0) {
		wspolrzedne_t puste;
		do {
			puste = Organizm::akcja(NULL);
		} while (getSwiat().getOrganizm(puste) != NULL);
		getSwiat().setOrganizm(stworzPotomka(), puste);
		getSwiat().zdarzenia.dodaj(toString()+" rozsiewa sie");
	}
	return polozenie;
}

void Roslina::rysuj() {
	std::cout << "|-|";
}

std::string Roslina::toString() const {
	return "Roslina";
}