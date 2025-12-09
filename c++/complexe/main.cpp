#include <bits/stdc++.h>
#include "complexe.h"

using namespace std;

complexe::complexe() : re(0), im(0) {}

complexe::complexe(double x, double y) : re(x), im(y) {}

complexe::~complexe() {}

// Getters
double complexe::getRe() const { return re; }
double complexe::getIm() const { return im; }

// Setters
void complexe::setRe(double x) { re = x; }
void complexe::setIm(double y) { im = y; }

// Utility functions
void complexe::affiche() const {
    cout << re << " + " << im << "i\n";
}

double complexe::module() const {
    return sqrt(re*re + im*im);
}

double complexe::argument() const {
    return atan2(im, re); // angle in radians
}

void complexe::affiche_polaire() const {
    cout << "Module: " << module()
         << ", Argument: " << argument() << " radians\n";
}

void complexe::sum(double dx, double dy) {
    re += dx;
    im += dy;
}

// Operator overloading
complexe complexe::operator+(const complexe& o) const {
    return complexe(re + o.re, im + o.im);
}

complexe complexe::operator-(const complexe& o) const {
    return complexe(re - o.re, im - o.im);
}

complexe complexe::operator*(const complexe& o) const {
    return complexe(re*o.re - im*o.im, re*o.im + im*o.re);
}

complexe complexe::operator/(const complexe& o) const {
    double denom = o.re*o.re + o.im*o.im;
    return complexe(
        (re*o.re + im*o.im)/denom,
        (im*o.re - re*o.im)/denom
    );
}

// Output operator
ostream& operator<<(ostream& out, const complexe& c) {
    if (c.im<0)
        out << c.re <<" " << c.im << "i";
    else
        out << c.re << " + " << c.im << "i";
    return out;
}
#include <iostream>
#include "complexe.h"

using namespace std;

int main() {
    complexe a(2, 3);
    complexe b(4, -1);

    cout << "a = " << a << "\n";
    cout << "b = " << b << "\n\n";

    cout << "a + b = " << a + b << "\n";
    cout << "a - b = " << a - b << "\n";
    cout << "a * b = " << a * b << "\n";
    cout << "a / b = " << a / b << "\n\n";

    cout << "Module de a : " << a.module() << "\n";
    cout << "Argument de a : " << a.argument() << " radians\n";

    return 0;
}
