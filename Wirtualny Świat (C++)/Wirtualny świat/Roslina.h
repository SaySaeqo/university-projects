#pragma once
#include "Organizm.h"
class Roslina :
	public Organizm
{
public:
	Roslina(Swiat&, const size_t sila, const wspolrzedne_t);
	Roslina(const Roslina&);

	virtual wspolrzedne_t akcja(const event_t) override;
	virtual void rysuj() override;
	virtual std::string toString() const override;

};

