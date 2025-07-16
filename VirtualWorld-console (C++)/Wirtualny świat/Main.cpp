using namespace std;
#include <iostream>
#include "Organizm.h"
#include "Swiat.h"
#include "Frame.h"
#include <conio.h>
//#define TEST

//	AUTOR:
//	Tomasz Piwowski 
//	nr indeksu 180171

int main(int argc, char* argv[]) {
#ifndef TEST
	Swiat numer_jeden(30, 20);
	Frame wirtualny_swiat(numer_jeden);
	wirtualny_swiat.graj();
#endif
#ifdef TEST
	cout << "0: " << (char)0 << " ";
	for (unsigned char c = 1; c != 0; c += 1) cout << (int)c << ": " << c << " ";
#endif

	return 0;
}