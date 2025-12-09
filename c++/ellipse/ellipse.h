#ifndef ELLIPSE_H
#define ELLIPSE_H

class Ellipse {
public:
    Ellipse();
    Ellipse(float x, float y, float a, float b);
    Ellipse(const Ellipse& e);
    virtual ~Ellipse();

    void deplace(double dx, double dy);
    void zoom(double z);

    double surface() const;


    virtual double perimetre() const;

    bool est_cercle() const;
    double distanceCentre(const Ellipse& other) const;

    // Display function should also be virtual
    virtual void affiche() const;

protected:
    double cX, cY;
    double a, b;
};

#endif
