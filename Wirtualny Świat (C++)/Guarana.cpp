#include "Guarana.h"
#include <iostream>


Guarana::Guarana(Swiat& swiat, const wspolrzedne_t polozenie) 
	: Roslina(swiat, 0, polozenie) { }
Guarana::Guarana(Guarana& org) : Roslina(org) { }


Organizm* Guarana::stworzPotomka() {
	return new Guarana(getSwiat(), { 0,0 });
}

void Guarana::obrona(Organizm& napastnik) {
	getSwiat().zdarzenia.dodaj(napastnik.toString()+" zjada "+toString()+" zyskujac nowe sily");
	napastnik.setSila(napastnik.getSila() + 3);
	getSwiat().ruszOrganizm(&napastnik, polozenie);
}

void Guarana::rysuj() {
	std::cout << char(-79) << char(-78) << char(-79);
}

std::string Guarana::toString() const {
	return "Guarana";
}