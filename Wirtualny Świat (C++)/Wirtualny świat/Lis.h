#pragma once
#include "Zwierze.h"
class Lis :
	public Zwierze
{
public:
	Lis(Swiat&, const wspolrzedne_t);
	Lis(Lis&);

	virtual Organizm* stworzPotomka() override;
	virtual wspolrzedne_t akcja(const event_t) override;
	virtual void rysuj() override;
	virtual std::string toString() const override;

};

