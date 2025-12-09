#include <bits/stdc++.h>
using namespace std;

// ==================== CLASS person ====================
class person {
protected:
    string nom, position;
    double salaire;

public:
    person() : nom(""), position(""), salaire(0) {}
    person(string nom, string position, double salaire)
        : nom(nom), position(position), salaire(salaire) {}

    virtual ~person() {}

    virtual void afficherPerson() const {
        cout << "Nom: " << nom
             << " | Poste: " << position
             << " | Salaire: " << salaire << endl;
    }

    virtual void saisirPerson() {
        cout << "Entrer nom, position, salaire:\n";
        cin >> nom >> position >> salaire;
    }
};


// ==================== CLASS boss ====================
class boss : public person {
protected:
    double ba;   // bonus annuel
    string mv;   // motivation

public:
    boss() : person(), ba(0), mv("") {}

    boss(string nom, string position, double salaire, double ba, string mv)
        : person(nom, position, salaire), ba(ba), mv(mv) {}

    ~boss() {}

    void afficherPerson() const override {
        cout << "Boss: " << nom << " | Poste: " << position
             << " | Salaire: " << salaire
             << " | Bonus Annuel: " << ba
             << " | Motivation: " << mv << endl;
    }

    void saisirPerson() override {
        cout << "Entrer nom, position, salaire, bonus annuel, motivation:\n";
        cin >> nom >> position >> salaire >> ba >> mv;
    }
};


// ==================== CLASS listperson ====================
class listperson {
protected:
    vector<person*> v;

public:
    listperson() {
        v.reserve(10);
    }

    ~listperson() {
        for (auto p : v) delete p;
    }

    void remplirlistperson() {
        char choix = 'O';

        while (choix == 'O' || choix == 'o') {

            cout << "\nAjouter : (1) Person  |  (2) Boss  ? ";
            int type;
            cin >> type;

            person* p = nullptr;

            if (type == 1)
                p = new person();
            else
                p = new boss();

            p->saisirPerson();
            v.push_back(p);

            cout << "Voulez-vous continuer ? (O/N) : ";
            cin >> choix;
        }
    }

    void afficherlistPerson() const {
        cout << "\n===== LISTE DES PERSONNES =====\n";
        for (auto p : v)
            p->afficherPerson();
    }
};


// ==================== MAIN ====================
int main() {
    listperson lp;

    lp.remplirlistperson();
    lp.afficherlistPerson();

    return 0;
}

