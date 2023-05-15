#pragma once
using namespace std;
#include <string>
#include <iostream>
#include <vector>
class SluchaczZdarzen
{
private:
	vector<string> kolejka;
	friend ostream& operator<<(ostream&, const SluchaczZdarzen&);

public:
	void dodaj(string&&);
	void wyczysc();

};

