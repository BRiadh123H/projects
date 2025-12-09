#ifndef CERCLE_H
#define CERCLE_H

#include "ellipse.h"

class Cercle : public Ellipse {
public:
    Cercle();
    Cercle(float x, float y, float diametre);
    ~Cercle();


    double rayon() const;


    double diametre() const;


    double perimetre() const override;  // Exact formula for circle

    void affiche() const override;
};

#endif
