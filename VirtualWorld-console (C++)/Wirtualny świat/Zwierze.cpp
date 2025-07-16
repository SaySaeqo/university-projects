#include "Zwierze.h"
#include <iostream>
#include<cstdlib>
#define JAK_CZESTO_ZAPLADNIA 2

Zwierze::Zwierze(Swiat& swiat, const size_t inicjatywa, const size_t sila, const wspolrzedne_t polozenie) : 
	Organizm(swiat,inicjatywa,sila,polozenie) { }
Zwierze::Zwierze(const Zwierze& org) : 
	Organizm(org) { }


bool Zwierze::przedAtakiem(Organizm& ofiara) {
	if (this->toString() == ofiara.toString() && rand() % JAK_CZESTO_ZAPLADNIA == 0) {
		if (szukajWolnegoPola()) {
			wspolrzedne_t puste;
			do {
				puste = Zwierze::akcja(NULL);
			} while (getSwiat().getOrganizm(puste) != NULL);
			getSwiat().setOrganizm(stworzPotomka(), puste);
			getSwiat().zdarzenia.dodaj(toString()+" kopuluje: sukces");
			return true;
		}
		return true;
	}
	else return Organizm::przedAtakiem(ofiara);
}

void Zwierze::rysuj() {
	std::cout << "[-]";
}

std::string Zwierze::toString() const {
	return "Zwierze";
}