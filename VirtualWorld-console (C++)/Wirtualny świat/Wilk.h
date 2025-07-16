#pragma once
#include "Zwierze.h"
class Wilk :
	public Zwierze
{
public:
	Wilk(Swiat&, const wspolrzedne_t);
	Wilk(Wilk&);

	virtual Organizm* stworzPotomka() override;
	virtual void rysuj() override;
	virtual std::string toString() const override;

};

