#pragma once
#include "Roslina.h"
class Trawa :
	public Roslina
{
public:
	Trawa(Swiat&, const wspolrzedne_t);
	Trawa(Trawa&);

	virtual Organizm* stworzPotomka() override;
	virtual void rysuj() override;
	virtual std::string toString() const override;


};

