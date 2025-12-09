#include<bits/stdc++.h>
using namespace std;

class Compte {
    double solde;

public:
    Compte() { solde = 0; }
    Compte(double m) : solde(m) {}

    void afficher_compte() {
        cout << "solde = " << solde << endl;
    }

    friend void operator+=(Compte&, double);
    friend Compte operator+(int x, const Compte& y);
    friend Compte operator+(const Compte& d, const Compte& y);
    friend ostream& operator<<(ostream& os, const Compte& d);

    Compte operator++();
    Compte operator++(int n);

    //----------------------------------------- NEW FUNCTION -----------------------------------------
    // Withdraw money safely
    void retirer(double montant) {
        if (montant <= 0) {
            cout << "Montant invalide !" << endl;
            return;
        }
        if (montant > solde) {
            cout << "Solde insuffisant !" << endl;
            return;
        }
        solde -= montant;
    }

    //----------------------------------------- NEW FUNCTION -----------------------------------------
    // Deposit money safely
    void deposer(double montant) {
        if (montant <= 0) {
            cout << "Montant invalide !" << endl;
            return;
        }
        solde += montant;
    }

    //----------------------------------------- NEW FUNCTION -----------------------------------------
    // Transfer money from this account to another
    void transferer(Compte& autre, double montant) {
        if (montant > solde) {
            cout << "Transfert impossible, solde insuffisant !" << endl;
            return;
        }
        solde -= montant;
        autre.solde += montant;
    }

    //----------------------------------------- NEW FUNCTION -----------------------------------------
    // Compare two accounts (returns true if this account has more money)
    bool operator>(const Compte& other) const {
        return solde > other.solde;
    }

    //----------------------------------------- NEW FUNCTION -----------------------------------------
    // Check equality of two accounts
    bool operator==(const Compte& other) const {
        return solde == other.solde;
    }
};

// PRE-INCREMENT
Compte Compte::operator++() {
    solde++;
    return *this;
}

// POST-INCREMENT
Compte Compte::operator++(int n) {
    Compte temp = *this;
    solde++;
    return temp;
}

ostream& operator<<(ostream& os, const Compte& d) {
    os << d.solde;
    return os;  // FIXED!
}

Compte operator+(int x, const Compte& y) {
    return Compte(x + y.solde);
}

Compte operator+(const Compte& d, const Compte& y) {
    return Compte(d.solde + y.solde);
}

void operator+=(Compte& c, double d) {
    c.solde += d;
}

int main() {

    cout << "===== CREATION DES COMPTES =====\n";
    Compte c;          // solde = 0
    Compte c1(50);     // solde = 50

    cout << "Compte c initial : ";
    c.afficher_compte();
    cout << "Compte c1 initial : ";
    c1.afficher_compte();


    cout << "\n===== OPERATEURS += ET + =====\n";
    c += 100;                  // friend operator+=
    operator+=(c, 123.5);      // direct call (rarely used but tested)

    cout << "c apres += 100 puis += 123.5 : ";
    c.afficher_compte();

    c1 = 5 + c;                // friend operator+(int, Compte)
    cout << "c1 = 5 + c  :  ";
    c1.afficher_compte();


    cout << "\n===== OPERATEURS ENTRE COMPTES =====\n";
    c1 = c1 + c;               // operator+(Compte, Compte)
    cout << "c1 = c1 + c  :  ";
    c1.afficher_compte();

    c1 = c1 + 200;             // implicit: Compte(200) + c1
    cout << "c1 = c1 + 200  :  ";
    c1.afficher_compte();


    cout << "\n===== OPERATEURS ++ =====\n";
    cout << "++c1  =  " << ++c1 << endl;  // pre-increment
    cout << "c1++  = " << c1++ << endl;  // post-increment
    cout << "Apres c1++ : " << c1 << endl;


    cout << "\n===== RETRAIT / DEPOT =====\n";
    cout << "Retrait de 50 de c1\n";
    c1.retirer(50);
    cout << "c1 = " << c1 << endl;

    cout << "Depot de 200 dans c1\n";
    c1.deposer(200);
    cout << "c1 = " << c1 << endl;


    cout << "\n===== TRANSFERT =====\n";
    cout << "Transfert de 100 de c1 vers c\n";
    c1.transferer(c, 100);

    cout << "c1 = " << c1 << endl;
    cout << "c  = " << c << endl;


    cout << "\n===== COMPARAISONS =====\n";
    cout << "c1 > c ?  =  " << (c1 > c ? "OUI" : "NON") << endl;
    cout << "c1 == c ? =  " << (c1 == c ? "OUI" : "NON") << endl;


    cout << "\n===== AFFICHAGE FINAL =====\n";
    cout << "c  = " << c << endl;
    cout << "c1 = " << c1 << endl;

    return 0;
}


