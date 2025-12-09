#include <iostream>
#include <cmath>
#include "ellipse.h"
using namespace std;

Ellipse::Ellipse() : cX(0), cY(0), a(1), b(2) {}

Ellipse::Ellipse(float x, float y, float a_, float b_)
    : cX(x), cY(y), a(a_), b(b_) {}

Ellipse::Ellipse(const Ellipse& e)
    : cX(e.cX), cY(e.cY), a(e.a), b(e.b) {}

Ellipse::~Ellipse() {}

void Ellipse::deplace(double dx, double dy) {
    cX += dx;
    cY += dy;
}

void Ellipse::zoom(double z) {
    a *= z;
    b *= z;
}

double Ellipse::surface() const {
    return M_PI * a * b;
}


double Ellipse::perimetre() const {
    // Ramanujan approximation
    double h = pow(a - b, 2) / pow(a + b, 2);
    return M_PI * (a + b) * (1 + (3 * h) / (10 + sqrt(4 - 3 * h)));
}


bool Ellipse::est_cercle() const {
    return fabs(a - b) < 1e-6;
}


double Ellipse::distanceCentre(const Ellipse& other) const {
    return sqrt(pow(cX - other.cX, 2) + pow(cY - other.cY, 2));
}

void Ellipse::affiche() const {
    cout << "Ellipse:\n";
    cout << "  Centre: (" << cX << ", " << cY << ")\n";
    cout << "  Axes: a=" << a << "  b=" << b << "\n";
}
