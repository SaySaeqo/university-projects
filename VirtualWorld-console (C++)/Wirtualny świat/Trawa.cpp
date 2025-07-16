#include "Trawa.h"
#include <iostream>


Trawa::Trawa(Swiat& swiat, const wspolrzedne_t polozenie) 
	: Roslina(swiat, 0, polozenie) { }
Trawa::Trawa(Trawa& org) : Roslina(org) { }


Organizm* Trawa::stworzPotomka() {
	return new Trawa(getSwiat(), { 0,0 });
}

void Trawa::rysuj() {
	std::cout << char(-79) << char(-79) << char(-79);
}

std::string Trawa::toString() const {
	return "Trawa";
}