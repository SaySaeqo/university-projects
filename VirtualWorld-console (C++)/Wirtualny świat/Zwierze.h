#pragma once
#include "Organizm.h"
class Zwierze :
	public Organizm
{
public:
	Zwierze(Swiat&, const size_t inicjatywa, const size_t sila, const wspolrzedne_t);
	Zwierze(const Zwierze&);

	virtual bool przedAtakiem(Organizm& ofiara) override;
	virtual void rysuj() override;
	virtual std::string toString() const override;

};

