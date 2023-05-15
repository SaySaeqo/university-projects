#pragma once
#include "Zwierze.h"
class Owca :
	public Zwierze
{
public:
	Owca(Swiat&, const wspolrzedne_t);
	Owca(Owca&);

	virtual Organizm* stworzPotomka() override;
	virtual void rysuj() override;
	virtual std::string toString() const override;

};

