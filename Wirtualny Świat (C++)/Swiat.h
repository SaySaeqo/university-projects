#pragma once
#include "SluchaczZdarzen.h"
#define K_ESC 27
#define K_SPECIAL 224
#define K_LEFT 75
#define K_RIGHT 77
#define K_UP 72
#define K_DOWN 80
#define K_SPACE 32
#define K_ENT 13
#define K_Q 113
#define POZA_MAPA {-1,-1}
typedef int event_t;

typedef struct {
	int x, y;
}wspolrzedne_t;

class Organizm;

class Swiat
{
private:
	wspolrzedne_t wymiary;
	size_t tura;
	Organizm*** plansza;
	Organizm* gracz;

public:
	SluchaczZdarzen zdarzenia;

	Swiat();
	Swiat(int);
	Swiat(int, int);
	~Swiat();

	wspolrzedne_t getWymiary() const;
	size_t getTura() const;
	Organizm& getGracz() const;
	size_t getOrganizmy(Organizm** const, const size_t rozm) const;
	Organizm* getOrganizm(wspolrzedne_t) const;
	void setOrganizm(Organizm*, wspolrzedne_t);
	void ruszOrganizm(Organizm*, wspolrzedne_t);

	void rysujSwiat() const;
	bool wykonajTure(event_t);

};

