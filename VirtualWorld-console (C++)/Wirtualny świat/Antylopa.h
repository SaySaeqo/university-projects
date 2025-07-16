#pragma once
#include "Zwierze.h"
class Antylopa :
	public Zwierze
{
public:
	Antylopa(Swiat&, const wspolrzedne_t);
	Antylopa(Antylopa&);

	virtual Organizm* stworzPotomka() override;
	virtual wspolrzedne_t akcja(const event_t) override;
	virtual bool przedAtakiem(Organizm& ofiara);
	virtual void obrona(Organizm& napastnik);
	virtual void rysuj() override;
	virtual std::string toString() const override;

};

