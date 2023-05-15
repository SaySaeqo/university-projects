#pragma once
#include "Zwierze.h"
class Zolw :
	public Zwierze
{
public:
	Zolw(Swiat&, const wspolrzedne_t);
	Zolw(Zolw&);

	virtual Organizm* stworzPotomka() override;
	virtual wspolrzedne_t akcja(const event_t) override;
	virtual void obrona(Organizm& napastnik) override;
	virtual void rysuj() override;
	virtual std::string toString() const override;

};

