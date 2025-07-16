#include "Organizm.h"
#include "Zwierze.h"
#include <iostream>


Organizm::Organizm(Swiat& swiat, const size_t inicjatywa, const size_t sila, const wspolrzedne_t polozenie) {
	this->swiat = &swiat;
	this->inicjatywa = inicjatywa;
	this->wiek = 0;
	this->sila = sila;
	this->polozenie = polozenie;
	czy_zyje = true;
}

Organizm::Organizm(const Organizm& org) {
	this->swiat = org.swiat;
	this->inicjatywa = org.inicjatywa;
	this->wiek = 0;
	this->sila = org.sila;
	this->polozenie = org.polozenie;
	this->czy_zyje = true;
}


wspolrzedne_t Organizm::getPolozenie() const {
	return polozenie;
}

size_t Organizm::getInicjatywa() const {
	return inicjatywa;
}

size_t Organizm::getSila() const {
	return sila;
}

Swiat& Organizm::getSwiat() const {
	return *swiat;
}

bool Organizm::czyUmarl() const {
	return !czy_zyje;
}

void Organizm::setPolozenie(wspolrzedne_t wsp) {
	this->polozenie.x = wsp.x;
	this->polozenie.y = wsp.y;
}

void Organizm::setSila(size_t sila) {
	this->sila = sila;
}


wspolrzedne_t Organizm::akcja(const event_t event) {
	wspolrzedne_t wynik = polozenie;
	wspolrzedne_t poza_mapa = POZA_MAPA;
	while (polozenie.x == wynik.x && polozenie.y == wynik.y) {
		wspolrzedne_t tmp = pole((kierunek_t)(rand() % KIERUNKI));
		if (tmp.x != poza_mapa.x && tmp.y != poza_mapa.y) wynik = tmp;
	}
	return wynik;
}

// proba ucieczki - jesli true trzeba zdefiniowaæ, gdzie ucieknie
bool Organizm::przedAtakiem(Organizm& ofiara) {
	return false; 
}

void Organizm::poAtaku() { }

void Organizm::obrona(Organizm& napastnik) {
	if(napastnik.przedAtakiem(*this)) return;	// czy udana proba ucieczki
	if (sila > napastnik.sila) {
		getSwiat().zdarzenia.dodaj(toString() + " kontraatakuje, zabijajac " + napastnik.toString());
		napastnik.smierc();
	}
	else {
		if(dynamic_cast<Zwierze*>(this))
			getSwiat().zdarzenia.dodaj(napastnik.toString() + " zabija " + toString());
		else
			getSwiat().zdarzenia.dodaj(napastnik.toString() + " niszczy/zjada " + toString());
		getSwiat().ruszOrganizm(&napastnik, polozenie);
	}
	napastnik.poAtaku();
}

void Organizm::rysuj() {
	std::cout << "[x]";
}

void Organizm::postarz() {
	wiek += 1;
}

void Organizm::smierc() {
	getSwiat().setOrganizm(NULL, polozenie);
	czy_zyje = false;
	if (wiek == 0) delete this;
}

std::string Organizm::toString() const {
	return "Organizm";
}


int Organizm::compareOrganizm(const void* mniejszy, const void* wiekszy) {
	const Organizm** a = (const Organizm**)mniejszy;
	const Organizm** b = (const Organizm**)wiekszy;
	if ((*a)->inicjatywa == (*b)->inicjatywa) return (int)((*b)->wiek - (*a)->wiek);
	else return (int)((*b)->inicjatywa - (*a)->inicjatywa);
}

wspolrzedne_t Organizm::pole(kierunek_t kierunek) const {
	wspolrzedne_t wynik = polozenie;
	switch (kierunek) {
	case LEWO:
		wynik.x--;
		break;
	case PRAWO:
		wynik.x++;
		break;
	case GORA:
		wynik.y--;
		break;
	case DOL:
		wynik.y++;
		break;
	case LEWO_GORA:
		wynik.x--;
		wynik.y--;
		break;
	case LEWO_DOL:
		wynik.x--;
		wynik.y++;
		break;
	case PRAWO_GORA:
		wynik.x++;
		wynik.y--;
		break;
	case PRAWO_DOL:
		wynik.x++;
		wynik.y++;
		break;
	}
	if (wynik.x < 0 || wynik.x >= getSwiat().getWymiary().x
		|| wynik.y < 0 || wynik.y >= getSwiat().getWymiary().y) return POZA_MAPA;
	else return wynik;
}

bool Organizm::szukajWolnegoPola() const {
	wspolrzedne_t tmp;
	wspolrzedne_t poza_mapa = POZA_MAPA;
	tmp = pole(PRAWO);
	if (tmp.x != poza_mapa.x && tmp.y != poza_mapa.y) {
		Organizm* pole = getSwiat().getOrganizm(tmp);
		if (pole == NULL) return true;
	}
	tmp = pole(LEWO);
	if (tmp.x != poza_mapa.x && tmp.y != poza_mapa.y) {
		Organizm* pole = getSwiat().getOrganizm(tmp);
		if (pole == NULL) return true;
	}
	tmp = pole(GORA);
	if (tmp.x != poza_mapa.x && tmp.y != poza_mapa.y) {
		Organizm* pole = getSwiat().getOrganizm(tmp);
		if (pole == NULL) return true;
	}
	tmp = pole(DOL);
	if (tmp.x != poza_mapa.x && tmp.y != poza_mapa.y) {
		Organizm* pole = getSwiat().getOrganizm(tmp);
		if (pole == NULL) return true;
	}
	tmp = pole(PRAWO_GORA);
	if (tmp.x != poza_mapa.x && tmp.y != poza_mapa.y) {
		Organizm* pole = getSwiat().getOrganizm(tmp);
		if (pole == NULL) return true;
	}
	tmp = pole(PRAWO_DOL);
	if (tmp.x != poza_mapa.x && tmp.y != poza_mapa.y) {
		Organizm* pole = getSwiat().getOrganizm(tmp);
		if (pole == NULL) return true;
	}
	tmp = pole(LEWO_GORA);
	if (tmp.x != poza_mapa.x && tmp.y != poza_mapa.y) {
		Organizm* pole = getSwiat().getOrganizm(tmp);
		if (pole == NULL) return true;
	}
	tmp = pole(LEWO_DOL);
	if (tmp.x != poza_mapa.x && tmp.y != poza_mapa.y) {
		Organizm* pole = getSwiat().getOrganizm(tmp);
		if (pole == NULL) return true;
	}

	return false;

}