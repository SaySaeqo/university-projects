#include "Lis.h"
#include <iostream>


Lis::Lis(Swiat& swiat, const wspolrzedne_t polozenie) 
	: Zwierze(swiat, 7, 3, polozenie) { }
Lis::Lis(Lis& org) : Zwierze(org) { }


Organizm* Lis::stworzPotomka() {
	return new Lis(getSwiat(), { 0,0 });
}

wspolrzedne_t Lis::akcja(const event_t event) {
	wspolrzedne_t pole;
	int licznik = 30;
	do {
		pole = Zwierze::akcja(event);
		licznik--;
	} while (licznik > 0 && getSwiat().getOrganizm(pole) != NULL && getSwiat().getOrganizm(pole)->getSila() > this->sila);
	if (licznik <= 0) {
		getSwiat().zdarzenia.dodaj(toString()+" decyduje sie nie ruszac");
		return polozenie;
	}
	else return pole;
}

void Lis::rysuj() {
	std::cout << "[L]";
}

std::string Lis::toString() const {
	return "Lis";
}