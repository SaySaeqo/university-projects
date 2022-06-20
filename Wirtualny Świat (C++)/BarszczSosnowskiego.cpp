#include "BarszczSosnowskiego.h"
#include"Zwierze.h"
#include <iostream>


BarszczSosnowskiego::BarszczSosnowskiego(Swiat& swiat, const wspolrzedne_t polozenie) 
	: Roslina(swiat, 10, polozenie) { }
BarszczSosnowskiego::BarszczSosnowskiego(BarszczSosnowskiego& org) 
	: Roslina(org) { }


Organizm* BarszczSosnowskiego::stworzPotomka() {
	return new BarszczSosnowskiego(getSwiat(), { 0,0 });
}

wspolrzedne_t BarszczSosnowskiego::akcja(const event_t) {
	wspolrzedne_t tmp;
	wspolrzedne_t poza_mapa = POZA_MAPA;
	Organizm* organizm;

	for (int i = LEWO; i < KIERUNKI; i++) {
		tmp = pole((kierunek_t)i);
		if (tmp.x != poza_mapa.x && tmp.y != poza_mapa.y) {
			organizm = getSwiat().getOrganizm(tmp);
			if (dynamic_cast<Zwierze*>(organizm)) {
				getSwiat().zdarzenia.dodaj(organizm->toString()+" umiera caly pokaleczony i w bablach :c");
				organizm->smierc();
			}
		}
	}
	return polozenie;
}

void BarszczSosnowskiego::obrona(Organizm& napastnik) {
	getSwiat().zdarzenia.dodaj(napastnik.toString()+" zjada "+toString()+" i umiera");
	this->smierc();
	napastnik.smierc();
}

void BarszczSosnowskiego::rysuj() {
	std::cout << char(-37) << char(-37) << char(-37);
}

std::string BarszczSosnowskiego::toString() const {
	return "BarszczSosnowskiego";
}