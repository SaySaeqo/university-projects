#pragma once
#include "Roslina.h"
class Guarana :
	public Roslina
{
public:
	Guarana(Swiat&, const wspolrzedne_t);
	Guarana(Guarana&);

	virtual Organizm* stworzPotomka() override;
	virtual void obrona(Organizm& napastnik) override;
	virtual void rysuj() override;
	virtual std::string toString() const override;


};

