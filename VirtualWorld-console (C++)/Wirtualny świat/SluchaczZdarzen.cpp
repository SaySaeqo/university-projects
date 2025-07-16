#include "SluchaczZdarzen.h"


void SluchaczZdarzen::dodaj(string&& zdanie) {
	kolejka.push_back(zdanie);
}

void SluchaczZdarzen::wyczysc() {
	kolejka.clear();
}


ostream& operator<<(ostream& ost, const SluchaczZdarzen& zdarzenia) {
	for (string zdanie : zdarzenia.kolejka) {
		ost << endl << "\t" << zdanie;
	}
	return ost;
}