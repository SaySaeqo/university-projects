#include "Zolw.h"
#include <iostream>


Zolw::Zolw(Swiat& swiat, const wspolrzedne_t polozenie) 
	: Zwierze(swiat, 1, 2, polozenie) { }
Zolw::Zolw(Zolw& org) : Zwierze(org) { }


Organizm* Zolw::stworzPotomka() {
	return new Zolw(getSwiat(), { 0,0 });
}

wspolrzedne_t Zolw::akcja(const event_t event) {
	if (rand() % 4) return polozenie;
	else return Zwierze::akcja(event);
}

void Zolw::obrona(Organizm& napastnik) {
	if (napastnik.przedAtakiem(*this)) return;	// czy udana proba ucieczki
	if (napastnik.getSila() >= 5) {
		getSwiat().zdarzenia.dodaj(napastnik.toString() + " zabija " + toString());
		getSwiat().ruszOrganizm(&napastnik, polozenie);
	}
	else {
		getSwiat().zdarzenia.dodaj(napastnik.toString()+" odbija sie od skorupy");
	}
	napastnik.poAtaku();
}

void Zolw::rysuj() {
	std::cout << "[Z]";
}

std::string Zolw::toString() const {
	return "Zolw";
}