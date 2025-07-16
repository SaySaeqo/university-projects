#pragma once
#include "Roslina.h"
class BarszczSosnowskiego :
	public Roslina
{
public:
	BarszczSosnowskiego(Swiat&, const wspolrzedne_t);
	BarszczSosnowskiego(BarszczSosnowskiego&);

	virtual Organizm* stworzPotomka() override;
	virtual wspolrzedne_t akcja(const event_t) override;
	virtual void obrona(Organizm& napastnik) override;
	virtual void rysuj() override;
	virtual std::string toString() const override;


};

