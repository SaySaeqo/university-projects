#pragma once
#include "Zwierze.h"
class Czlowiek :
	public Zwierze
{
private:
	int punkty;
	int oczekiwanie_na_specjalna_umiejetnosc;
public:
	Czlowiek(Swiat&, const wspolrzedne_t);
	Czlowiek(const Czlowiek&);

	size_t getPunkty() const;
	void przyznajPunkt();

	virtual Organizm* stworzPotomka() override;
	virtual wspolrzedne_t akcja(const event_t) override;
	virtual void poAtaku() override;
	virtual void obrona(Organizm& napastnik) override;
	virtual void rysuj() override;
	virtual std::string toString() const override;

};

