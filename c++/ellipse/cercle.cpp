#include <iostream>
#include <cmath>
#include "cercle.h"
using namespace std;

Cercle::Cercle() : Ellipse(0, 0, 1, 1) {}

Cercle::Cercle(float x, float y, float diam)
    : Ellipse(x, y, diam / 2, diam / 2) {}   // a = b = radius

Cercle::~Cercle() {}


double Cercle::rayon() const {
    return a;
}


double Cercle::diametre() const {
    return 2 * a;
}


double Cercle::perimetre() const {
    return 2 * M_PI * a;
}

void Cercle::affiche() const {
    cout << "Cercle:\n";
    cout << "  Centre: (" << cX << ", " << cY << ")\n";
    cout << "  Rayon: " << a << "\n";
}
