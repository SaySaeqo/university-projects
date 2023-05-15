#pragma once
#include "Roslina.h"
class WilczeJagody :
	public Roslina
{
public:
	WilczeJagody(Swiat&, const wspolrzedne_t);
	WilczeJagody(WilczeJagody&);

	virtual Organizm* stworzPotomka() override;
	virtual void obrona(Organizm& napastnik) override;
	virtual void rysuj() override;
	virtual std::string toString() const override;


};

