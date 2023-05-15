#pragma once
#include "Swiat.h"
#include<ctime>

class Frame
{
private:
	Swiat* swiat;
	wspolrzedne_t wymiary;
	bool pokazMenu();
	void zakonczGre();

public:
	Frame(Swiat&);
	~Frame();

	void graj();

	static void czekaj(clock_t);

};

