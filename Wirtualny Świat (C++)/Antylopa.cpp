#include "Antylopa.h"
#include <iostream>


Antylopa::Antylopa(Swiat& swiat, const wspolrzedne_t polozenie) 
	: Zwierze(swiat, 4, 4, polozenie) { }
Antylopa::Antylopa(Antylopa& org) 
	: Zwierze(org) { }


Organizm* Antylopa::stworzPotomka() {
	return new Antylopa(getSwiat(), { 0,0 });
}

wspolrzedne_t Antylopa::akcja(const event_t event) {
	if (szukajWolnegoPola()) {
		wspolrzedne_t puste;
		do {
			puste = Zwierze::akcja(NULL);
		} while (getSwiat().getOrganizm(puste) != NULL);
		getSwiat().ruszOrganizm(this, puste);
		//getSwiat().zdarzenia.dodaj(toString()+" rusza sie dwukrotnie");
	}
	return Zwierze::akcja(event);
}

bool Antylopa::przedAtakiem(Organizm& ofiara) {
	if( Zwierze::przedAtakiem(ofiara)) return true; // prokreacja
	if (rand() % 2 == 0) return false;
	else {
		if (szukajWolnegoPola() == false) return false;
		wspolrzedne_t puste;
		do {
			puste = Zwierze::akcja(NULL);
		} while (getSwiat().getOrganizm(puste) != NULL);
		getSwiat().ruszOrganizm(this, puste);
		getSwiat().zdarzenia.dodaj(toString()+" unika starcia z "+ofiara.toString());
		return  true;
	}
}

void Antylopa::obrona(Organizm& napastnik) {
	if (przedAtakiem(napastnik)) return;
	else Zwierze::obrona(napastnik);
}

void Antylopa::rysuj() {
	std::cout << "[A]";
}

std::string Antylopa::toString() const {
	return "Antylopa";
}