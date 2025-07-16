#include "Czlowiek.h"
#include<iostream>


Czlowiek::Czlowiek(Swiat& swiat, const wspolrzedne_t polozenie) : 
	Zwierze(swiat, 4, 5, polozenie) {
	punkty = 0;
	oczekiwanie_na_specjalna_umiejetnosc = 0;
}
Czlowiek::Czlowiek(const Czlowiek& org) :
	Zwierze(org) { 
	this->punkty = org.punkty; 
	oczekiwanie_na_specjalna_umiejetnosc = org.oczekiwanie_na_specjalna_umiejetnosc;
}


size_t Czlowiek::getPunkty() const { 
	return punkty;
}

void Czlowiek::przyznajPunkt() { 
	punkty += 1;
}


Organizm* Czlowiek::stworzPotomka() {
	return new Czlowiek(getSwiat(), { 0,0 });
}

wspolrzedne_t Czlowiek::akcja(const event_t event) {
	wspolrzedne_t wynik = polozenie;
	wspolrzedne_t poza_mapa = POZA_MAPA;
	switch (event)
	{
	case K_UP:
		wynik = pole(GORA);
		break;
	case K_DOWN:
		wynik = pole(DOL);
		break;
	case K_RIGHT:
		wynik = pole(PRAWO);
		break;
	case K_LEFT:
		wynik = pole(LEWO);
		break;
	case K_SPACE:
		if (oczekiwanie_na_specjalna_umiejetnosc == 0) {
			oczekiwanie_na_specjalna_umiejetnosc = 10;
			getSwiat().zdarzenia.dodaj("WYPILES ELIKSIR. ROZPIERA CIE NOWA SILA");
		}
		break;
	}
	// MAGICZNY ELIKSIR
	if (oczekiwanie_na_specjalna_umiejetnosc > 0) {
		if (oczekiwanie_na_specjalna_umiejetnosc == 10) {
			sila = 10;
		}
		else if (oczekiwanie_na_specjalna_umiejetnosc >= 5) {
			sila -= 1;
			
		}
		oczekiwanie_na_specjalna_umiejetnosc -= 1;
		if(oczekiwanie_na_specjalna_umiejetnosc==0)
			getSwiat().zdarzenia.dodaj("ZNOWU MOZESZ WYPIC ELIKSIR");
	}
	// --------------
	if (wynik.x != poza_mapa.x && wynik.y != poza_mapa.y) return wynik;
	else return polozenie;
}

void Czlowiek::poAtaku() { 
	if (!czyUmarl()) {
		przyznajPunkt();
		getSwiat().zdarzenia.dodaj("Przyznano punkt!");
	}

}

void Czlowiek::obrona(Organizm& napastnik) {
	Zwierze::obrona(napastnik);
	if (!czyUmarl()) {
		przyznajPunkt();
		getSwiat().zdarzenia.dodaj("Przyznano punkt!");
	}
}

void Czlowiek::rysuj() { 
	std::cout << '/'<<(char)(-34)<<'\\';
}

std::string Czlowiek::toString() const { 
	return "Czlowiek";
}