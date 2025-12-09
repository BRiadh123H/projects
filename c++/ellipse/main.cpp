#include <iostream>
#include "ellipse.h"
#include "cercle.h"
using namespace std;

int main() {

    cout << "===== ELLIPSE =====\n";
    Ellipse e(10, 5, 6, 4);
    e.affiche();

    cout << "Surface = " << e.surface() << endl;
    cout << "Perimetre (approx) = " << e.perimetre() << endl;
    cout << "Est un cercle ? " << (e.est_cercle() ? "OUI" : "NON") << endl;

    e.deplace(10, -5);
    e.zoom(1.5);
    cout << "\nApres deplacement et zoom :\n";
    e.affiche();


    cout << "\n===== CERCLE =====\n";
    Cercle c(0, 0, 10);
    c.affiche();

    cout << "Rayon = " << c.rayon() << endl;
    cout << "Diametre = " << c.diametre() << endl;
    cout << "Perimetre = " << c.perimetre() << endl;


    cout << "\n===== DISTANCE ENTRE CENTRES =====\n";
    cout << "Distance(e, c) = " << e.distanceCentre(c) << endl;

    return 0;
}
