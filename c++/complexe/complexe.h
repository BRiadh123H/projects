#ifndef COMPLEXE_H
#define COMPLEXE_H

class complexe {
private:
    double re, im;

public:
    // Constructors
    complexe();
    complexe(double x, double y);

    // Destructor
    ~complexe();

    // Getters
    double getRe() const;
    double getIm() const;

    // Setters
    void setRe(double x);
    void setIm(double y);

    // Utility functions
    void affiche() const;
    void affiche_polaire() const;

    double module() const;
    double argument() const;

    // Operations
    void sum(double dx, double dy);

    // Operator overloading
    complexe operator+(const complexe& other) const;
    complexe operator-(const complexe& other) const;
    complexe operator*(const complexe& other) const;
    complexe operator/(const complexe& other) const;

    // Friend function for output
    friend std::ostream& operator<<(std::ostream& out, const complexe& c);
};

#endif
