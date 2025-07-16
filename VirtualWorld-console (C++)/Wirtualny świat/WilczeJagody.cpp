#include "WilczeJagody.h"
#include <iostream>


WilczeJagody::WilczeJagody(Swiat& swiat, const wspolrzedne_t polozenie) 
	: Roslina(swiat, 99, polozenie) { }
WilczeJagody::WilczeJagody(WilczeJagody& org) : Roslina(org) { }


Organizm* WilczeJagody::stworzPotomka() {
	return new WilczeJagody(getSwiat(), { 0,0 });
}

void WilczeJagody::obrona(Organizm& napastnik) {
	getSwiat().zdarzenia.dodaj(napastnik.toString()+" zjada "+toString()+" i umiera");
	this->smierc();
	napastnik.smierc();
}

void WilczeJagody::rysuj() {
	std::cout << char(-78) << char(-78) << char(-78);
}

std::string WilczeJagody::toString() const {
	return "WilczeJagody";
}