using namespace std;
#include "Swiat.h"
#include "Czlowiek.h"
#include "Antylopa.h"
#include "BarszczSosnowskiego.h"
#include "Guarana.h"
#include "Lis.h"
#include "Mlecz.h"
#include "Owca.h"
#include "Trawa.h"
#include "WilczeJagody.h"
#include "Wilk.h"
#include "Zolw.h"
#include "Zwierze.h"
#include "Roslina.h"
#include "Frame.h"
#include "Organizm.h"
#include <ctime>
#include<cstdlib>
#include<iostream>
#include <conio.h>
#include <iomanip>
#include <algorithm>
#define AUTOR "Tomasz_Piwowski_180171"
#define START_X 0
#define START_Y (wymiary.y - 1)


Swiat::Swiat() : Swiat(20,20) {}
Swiat::Swiat(int x) : Swiat(x,x) {}
Swiat::Swiat(int x, int y){
	if (x < 10) x = 10;
	if (y == 0) y = 1;
	wymiary.x = x;
	wymiary.y = y;
	tura = 0;
	plansza = new Organizm ** [wymiary.x];
	for (size_t i = 0; i < wymiary.x; i++) {
		plansza[i] = new Organizm*[wymiary.y];
	}
	gracz = new Czlowiek(*this, { START_X,START_Y });
	plansza[START_X][START_Y] = gracz;
	srand((unsigned)time(NULL));
	for (int i = 0; i < wymiary.x; i++) {
		for (int j = 0; j < wymiary.y; j++) {
			if (i == START_X && j == START_Y) continue;
			switch (rand()%(GATUNKI*10)) {
			case ANTYLOPA:
				plansza[i][j] = new Antylopa(*this, { i,j });
				break;
			case BARSZCZ_SOSNOWSKIEGO:
				if (rand() % 4) plansza[i][j] = new BarszczSosnowskiego(*this, { i,j });
				else plansza[i][j] = NULL;
				break;
			case GUARANA:
				plansza[i][j] = new Guarana(*this, { i,j });
				break;
			case LIS:
				plansza[i][j] = new Lis(*this, { i,j });
				break;
			case MLECZ:
				plansza[i][j] = new Mlecz(*this, { i,j });
				break;
			case OWCA:
				plansza[i][j] = new Owca(*this, { i,j });
				break;
			case TRAWA:
				plansza[i][j] = new Trawa(*this, { i,j });
				break;
			case WILCZE_JAGODY:
				plansza[i][j] = new WilczeJagody(*this, { i,j });
				break;
			case WILK:
				plansza[i][j] = new Wilk(*this, { i,j });
				break;
			case ZOLW:
				plansza[i][j] = new Zolw(*this, { i,j });
				break;
			default:
				plansza[i][j] = NULL;
				break;
			}
		}
	}

}

Swiat::~Swiat() {
	if (gracz->czyUmarl()) delete gracz;
	for (size_t i = 0; i < wymiary.x; i++) {
		for (size_t j = 0; j < wymiary.y; j++) {
			delete plansza[i][j];
		}
		delete[] plansza[i];
	}
	delete[] plansza;
}


wspolrzedne_t Swiat::getWymiary() const {
	return wymiary;
}

size_t Swiat::getTura() const {
	return tura;
}

Organizm& Swiat::getGracz() const {
	return *gracz;
}

size_t Swiat::getOrganizmy(Organizm** const tab, const size_t rozm) const {
	Organizm** organizmy = new Organizm * [(size_t)wymiary.x * (size_t)wymiary.y];
	size_t iter = 0;
	for (size_t i = 0; i < wymiary.x; i++) {
		for (size_t j = 0; j < wymiary.y; j++) {
			if (plansza[i][j] != NULL)	organizmy[iter++] = plansza[i][j];
		}
	}
	qsort(organizmy,iter,sizeof(Organizm*),Organizm::compareOrganizm);
	for (size_t i = 0; i < rozm; i++) {
		for (i; i < iter; i++) {
			tab[i] = organizmy[i];
		}
		if (i >= rozm) break;
		tab[i] = NULL;
	}
	delete[] organizmy;
	return iter;

}

Organizm* Swiat::getOrganizm(wspolrzedne_t wsp) const {
	return plansza[wsp.x][wsp.y];
}

void Swiat::setOrganizm(Organizm* organizm, wspolrzedne_t wspolrzedne) {
	plansza[wspolrzedne.x][wspolrzedne.y] = organizm;
	if (organizm != NULL) organizm->setPolozenie(wspolrzedne);
}

void Swiat::ruszOrganizm(Organizm* organizm, wspolrzedne_t wspolrzedne) {
	if (organizm != getOrganizm(organizm->getPolozenie())) {
		cout << endl << "Obiekt " << organizm->toString() << " nie jest na swojej pozycji";
		exit(1);
	}
	if (wspolrzedne.x == organizm->getPolozenie().x && wspolrzedne.y == organizm->getPolozenie().y)	return;
	if (getOrganizm(wspolrzedne) != NULL)getOrganizm(wspolrzedne)->smierc();
	setOrganizm(NULL, organizm->getPolozenie());
	setOrganizm(organizm, wspolrzedne);
}


void Swiat::rysujSwiat() const {
	system("cls");
#ifdef AUTOR
	cout << "   " << AUTOR << endl;
#endif
	cout << "   " << setw((size_t)wymiary.x * 3) << right << "Q -  quit" << endl;
	cout << "   " << setw((size_t)wymiary.x * 3) << right << "ARROWS - move" << endl;
	cout << "   " << "TURA: " << setw(4) << tura << setw((size_t)wymiary.x * 3 - 10) << right << "SPACE - special" << endl;

	for (size_t y = 0; y < wymiary.y; y++) {

		cout << endl << "   ";
		for (size_t x = 0; x < wymiary.x; x++) {
			if (plansza[x][y] == NULL)	cout << char(-60) << char(-59) << char(-60);
			else plansza[x][y]->rysuj();
		}
		cout << "   ";
	}

	cout << "\r   ";

}

bool Swiat::wykonajTure(event_t event) {
	if (event == K_ESC) exit(0);
	if (event == K_Q) return false;
	if (event != K_SPECIAL && event != K_SPACE)	return true;
	if (event == K_SPECIAL) {
		event = _getch();
		wspolrzedne_t poz = gracz->getPolozenie();
		if ((event == K_UP && poz.y == 0)
			|| (event == K_DOWN && poz.y == wymiary.y - 1)
			|| (event == K_RIGHT && poz.x == wymiary.x - 1)
			|| (event == K_LEFT && poz.x == 0)) return true;
	}

	Organizm** kolejka = new Organizm * [(size_t)wymiary.x * (size_t)wymiary.y];
	size_t rozm_kolejki = getOrganizmy(kolejka, (size_t)wymiary.x * (size_t)wymiary.y);

	for (size_t i = 0; i < rozm_kolejki; i++) kolejka[i]->postarz();

	bool czy_koniec = false;
	for (size_t i = 0; i < rozm_kolejki; i++) {
		if (kolejka[i]->czyUmarl()) continue;
		wspolrzedne_t nowe_wsp = kolejka[i]->akcja(event);
		wspolrzedne_t stare_wsp = kolejka[i]->getPolozenie();

		if (getOrganizm(nowe_wsp) != NULL
			&& (stare_wsp.x != nowe_wsp.x || stare_wsp.y != nowe_wsp.y))
		{
			getOrganizm(nowe_wsp)->obrona(*kolejka[i]);

		}
		else if (getOrganizm(nowe_wsp) == NULL) {
			ruszOrganizm(kolejka[i], nowe_wsp);
		}
		if (gracz->czyUmarl()) {
			czy_koniec = true;
			break;
		}
		
	}
	rysujSwiat();
	cout << endl << "    SILA GRACZA: " << gracz->getSila();
	cout << zdarzenia;
	cout << "\r";
	zdarzenia.wyczysc();
	tura++;
	
	size_t licz_zywych = rozm_kolejki;
	
	for (size_t i = 0; i < rozm_kolejki; i++) {
		if (kolejka[i] == gracz) continue;
		if (kolejka[i]->czyUmarl()) { 
			delete kolejka[i]; 
			licz_zywych--;
		}

	}
	delete[] kolejka;

	if (licz_zywych == 1 || czy_koniec) return false;
	else return true;
}