#include "Wilk.h"
#include <iostream>


Wilk::Wilk(Swiat& swiat, const wspolrzedne_t polozenie) 
	: Zwierze(swiat, 5, 9, polozenie) { }
Wilk::Wilk(Wilk& org) : Zwierze(org) { }


Organizm* Wilk::stworzPotomka() {
	return new Wilk(getSwiat(), { 0,0 });
}

void Wilk::rysuj() {
	std::cout << "[W]";
}

std::string Wilk::toString() const {
	return "Wilk";
}