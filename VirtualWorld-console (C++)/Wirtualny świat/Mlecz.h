#pragma once
#include "Roslina.h"
class Mlecz :
	public Roslina
{
public:
	Mlecz(Swiat&, const wspolrzedne_t);
	Mlecz(Mlecz&);

	virtual Organizm* stworzPotomka() override;
	virtual wspolrzedne_t akcja(const event_t) override;
	virtual void rysuj() override;
	virtual std::string toString() const override;


};

