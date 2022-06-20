#include "Mlecz.h"
#include <iostream>


Mlecz::Mlecz(Swiat& swiat, const wspolrzedne_t polozenie) 
	: Roslina(swiat, 0, polozenie) { }
Mlecz::Mlecz(Mlecz& org) : Roslina(org) { }


Organizm* Mlecz::stworzPotomka() {
	return new Mlecz(getSwiat(), { 0,0 });
}

wspolrzedne_t Mlecz::akcja(const event_t event) {
	Roslina::akcja(event);
	Roslina::akcja(event);
	return Roslina::akcja(event);
}

void Mlecz::rysuj() {
	std::cout << char(-80) << char(-80) << char(-80);
}

std::string Mlecz::toString() const {
	return "Mlecz";
}