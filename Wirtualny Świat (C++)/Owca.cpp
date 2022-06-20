#include "Owca.h"
#include <iostream>


Owca::Owca(Swiat& swiat, const wspolrzedne_t polozenie) 
	: Zwierze(swiat, 4, 4, polozenie) { }
Owca::Owca(Owca& org) : Zwierze(org) { }


Organizm* Owca::stworzPotomka() {
	return new Owca(getSwiat(), { 0,0 });
}

void Owca::rysuj() {
	std::cout << "[O]";
}

std::string Owca::toString() const {
	return "Owca";
}