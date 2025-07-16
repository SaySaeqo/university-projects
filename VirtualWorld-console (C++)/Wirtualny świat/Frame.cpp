using namespace std;
#include "Frame.h"
#include "Czlowiek.h"
#include <iostream>
#include <ctime>
#include <iomanip>
#include <conio.h>
#define WARTOSC 2


Frame::Frame(Swiat& swiat) {
	this->swiat = &swiat;
	wymiary = swiat.getWymiary();
}
Frame::~Frame() { }


bool Frame::pokazMenu() {

	bool czy_start = true;
	while (true) {
		system("cls");
		switch (czy_start) {
		case true:
			cout << endl;
			cout << setw(wymiary.x * WARTOSC) << internal << "------------------------" << endl;
			cout << setw(wymiary.x * WARTOSC) << internal << "         M E N U        " << endl;
			cout << setw(wymiary.x * WARTOSC) << internal << "------------------------" << endl;
			cout << setw(wymiary.x * WARTOSC) << internal << "0oooooooooooooooooooooo0" << endl;
			cout << setw(wymiary.x * WARTOSC) << internal << "0       Start Game     0" << endl;
			cout << setw(wymiary.x * WARTOSC) << internal << "0oooooooooooooooooooooo0" << endl;
			cout << setw(wymiary.x * WARTOSC) << internal << "          Quit          " << endl;
			cout << endl;
			break;
		case false:
			cout << endl;
			cout << setw(wymiary.x * WARTOSC) << internal << "------------------------" << endl;
			cout << setw(wymiary.x * WARTOSC) << internal << "         M E N U        " << endl;
			cout << setw(wymiary.x * WARTOSC) << internal << "------------------------" << endl;
			cout << endl;
			cout << setw(wymiary.x * WARTOSC) << internal << "        Start Game      " << endl;
			cout << setw(wymiary.x * WARTOSC) << internal << "0oooooooooooooooooooooo0" << endl;
			cout << setw(wymiary.x * WARTOSC) << internal << "0         Quit         0" << endl;
			cout << setw(wymiary.x * WARTOSC) << internal << "0oooooooooooooooooooooo0" << endl;
			break;
		}
		cout << setw(wymiary.x * (WARTOSC + 1)) << right << "ESC - quit" << endl;
		cout << setw(wymiary.x * (WARTOSC + 1)) << right << "ENTER, ARROWS - choose";
		cout << "\r";
		event_t event = _getch();
		while (true) {
			if (event == K_ESC) return false;
			if (event == K_ENT) return czy_start;
			if (event == K_SPECIAL) {
				event = _getch();
				if (event == K_UP || event == K_DOWN) {
					czy_start = !czy_start;
					break;
				}
			}
			event = _getch();
		}
	}
	
	event_t event = _getch();
}

void Frame::zakonczGre() {
	//kosmetyka
	cout << endl << "\t";
	czekaj(200);
	cout << 'C';
	czekaj(200);
	cout << 'O';
	czekaj(200);
	cout << "N";
	czekaj(200);
	cout << 'G';
	czekaj(200);
	cout << 'R';
	czekaj(200);
	cout << 'A';
	czekaj(400);
	cout << '.';
	czekaj(600);
	cout << '.';
	czekaj(800);
	cout << '.';
	czekaj(600);
	cout << "\r\t         \r\t";

	czekaj(400);
	cout << "\r";
	for (int i = 0; i < 8; i++) {
		czekaj(50);
		cout << " ";
	}
	czekaj(50);
	cout << "T";
	czekaj(50);
	cout << 'H';
	czekaj(50);
	cout << "E";
	czekaj(50);
	cout << " ";
	czekaj(50);
	cout << "E";
	czekaj(50);
	cout << "N";
	czekaj(50);
	cout << "D";
	czekaj(50);
	char c = '_';
	if (wymiary.x < 17) c = ' ';
	for (int i = 0; i < 8; i++) {
		czekaj(50);
		cout << c;
	}
	if (wymiary.x < 17) cout << endl;
	cout << ">\tSCORE:\t";
	cout.width(4);
	Czlowiek& gracz = (Czlowiek&)swiat->getGracz();
	cout << gracz.getPunkty();
	cout << endl;
	cout << "Nacisnij dowolny przycisk, by zakonczyc...";
	_getch();

}


void Frame::graj() {
	if (!pokazMenu()) return;
	swiat->rysujSwiat();
	event_t event;
	do {
		do {
			event = _getch();
		} while (event != K_ESC
			&& event != K_Q
			&& event != K_SPACE
			&& event != K_SPECIAL
			);
		czekaj(50);
	} while (swiat->wykonajTure(event));
	swiat->rysujSwiat();
	zakonczGre();
}

void Frame::czekaj(clock_t milli_sec) {
	clock_t teraz = clock();
	clock_t pozniej = clock();
	do {
		pozniej = clock();
	} while ((pozniej - teraz) <= milli_sec * CLOCKS_PER_SEC / 1000);

}