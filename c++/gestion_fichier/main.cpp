#include <bits/stdc++.h>
using namespace std;

class Article {
private:
    string reference;
    string nom;
    double prix;
    int quantite;

public:
    // Constructeurs
    Article() {}
    Article(string ref, string n, double p, int q)
        : reference(ref), nom(n), prix(p), quantite(q) {}

    // Getters
    string getReference() const { return reference; }
    string getNom() const { return nom; }
    double getPrix() const { return prix; }
    int getQuantite() const { return quantite; }

    // Setters
    void setNom(string n) { nom = n; }
    void setPrix(double p) { prix = p; }
    void setQuantite(int q) { quantite = q; }

    // toString
    string toString() const {
        return "Réf: " + reference + " | Nom: " + nom +
               " | Prix: " + to_string(prix) +
               " DH | Quantité: " + to_string(quantite);
    }
};

class Stock {
private:
    vector<Article> articles;

    int chercherIndexParReference(const string& ref) {
        for (int i = 0; i < articles.size(); i++)
            if (articles[i].getReference() == ref)
                return i;
        return -1;
    }

    // Comparateurs lisibles pour le tri
    static bool comparerPrixCroissant(const Article& a, const Article& b) { return a.getPrix() < b.getPrix(); }
    static bool comparerPrixDecroissant(const Article& a, const Article& b) { return a.getPrix() > b.getPrix(); }
    static bool comparerNom(const Article& a, const Article& b) { return a.getNom() < b.getNom(); }
    static bool comparerQuantite(const Article& a, const Article& b) { return a.getQuantite() < b.getQuantite(); }

public:
    // Vérifie si le stock est vide
    bool estVide() const { return articles.empty(); }

    // 1. Rechercher par référence
    void rechercherParReference() {
        string ref;
        cout << "Référence : ";
        cin >> ref;
        int i = chercherIndexParReference(ref);
        if (i != -1) cout << articles[i].toString() << endl;
        else cout << "❌ Article introuvable.\n";
    }

    // 2. Ajouter un article
    void ajouterArticle() {
        string ref, nom;
        double prix;
        int qte;
        cout << "Référence : "; cin >> ref;
        if (chercherIndexParReference(ref) != -1) {
            cout << " Référence déjà existante !\n";
            return;
        }
        cout << "Nom : "; cin >> nom;
        cout << "Prix : "; cin >> prix;
        cout << "Quantité : "; cin >> qte;
        articles.push_back(Article(ref, nom, prix, qte));
        cout << "Article ajouté.\n";
    }

    // 3. Afficher tous les articles
    void afficherArticles() {
        if (estVide()) { cout << " Le stock est vide.\n"; return; }
        for (const auto& a : articles) cout << a.toString() << endl;
    }

    // 4. Supprimer un article
    void supprimerArticle() {
        string ref;
        cout << "Référence : "; cin >> ref;
        int i = chercherIndexParReference(ref);
        if (i == -1) { cout << " Article introuvable.\n"; return; }
        articles.erase(articles.begin() + i);
        cout << "Article supprimé.\n";
    }

    // 5. Modifier un article
    void modifierArticle() {
        string ref; cout << "Référence : "; cin >> ref;
        int i = chercherIndexParReference(ref);
        if (i == -1) { cout << " Article introuvable.\n"; return; }
        string nom; double prix; int qte;
        cout << "Nouveau nom : "; cin >> nom;
        cout << "Nouveau prix : "; cin >> prix;
        cout << "Nouvelle quantité : "; cin >> qte;
        articles[i].setNom(nom); articles[i].setPrix(prix); articles[i].setQuantite(qte);
        cout << " Article modifié.\n";
    }

    // 6. Rechercher par nom
    void rechercherParNom() {
        string nom; cout << "Nom : "; cin >> nom;
        bool trouve = false;
        for (const auto& a : articles)
            if (a.getNom() == nom) { cout << a.toString() << endl; trouve = true; }
        if (!trouve) cout << " Aucun article trouvé.\n";
    }

    // 7. Rechercher par intervalle de prix
    void rechercherParPrix() {
        double min, max; cout << "Prix min : "; cin >> min; cout << "Prix max : "; cin >> max;
        bool trouve = false;
        for (const auto& a : articles)
            if (a.getPrix() >= min && a.getPrix() <= max) { cout << a.toString() << endl; trouve = true; }
        if (!trouve) cout << " Aucun article trouvé.\n";
    }

    // 8. Sauvegarde dans un fichier txt
    void sauvegarder() {
        ofstream fichier("D:\\stock.txt");
        if (!fichier) { cout << " Impossible d’ouvrir le fichier.\n"; return; }
        for (const auto& a : articles) fichier << a.toString() << endl;
        fichier.close(); cout << " Stock sauvegardé dans D:\\stock.txt\n";
    }

    // 9. Export CSV
    void exporterCSV() {
        ofstream f("D:\\stock.csv");
        if (!f) { cout << " Impossible d’ouvrir stock.csv\n"; return; }
        f << "Reference,Nom,Prix,Quantite\n";
        for (const auto& a : articles)
            f << a.getReference() << "," << a.getNom() << "," << a.getPrix() << "," << a.getQuantite() << "\n";
        f.close(); cout << " Export réalisé dans D:\\stock.csv\n";
    }

    // 10. Valeur totale du stock
    void valeurTotale() {
        double total = 0;
        for (const auto& a : articles) total += a.getPrix() * a.getQuantite();
        cout << " Valeur totale du stock : " << total << " DH\n";
    }

    // 11. Tri
    void trierParPrixCroissant() { sort(articles.begin(), articles.end(), comparerPrixCroissant); cout << " Tri prix croissant.\n"; }
    void trierParPrixDecroissant() { sort(articles.begin(), articles.end(), comparerPrixDecroissant); cout << " Tri prix décroissant.\n"; }
    void trierParNom() { sort(articles.begin(), articles.end(), comparerNom); cout << " Tri par nom.\n"; }
    void trierParQuantite() { sort(articles.begin(), articles.end(), comparerQuantite); cout << " Tri par quantité.\n"; }
};

int main() {
    Stock stock;
    int choix;

    do {
        cout << "\n===== MENU STOCK =====\n";
        cout << "1. Rechercher par reference\n";
        cout << "2. Ajouter un article\n";
        cout << "3. Afficher tous les articles\n";
        cout << "4. Supprimer un article\n";
        cout << "5. Modifier un article\n";
        cout << "6. Rechercher par nom\n";
        cout << "7. Rechercher par intervalle de prix\n";
        cout << "8. Sauvegarder dans un fichier txt\n";
        cout << "9. Exporter en CSV\n";
        cout << "10. Valeur totale du stock\n";
        cout << "11. Trier par prix croissant\n";
        cout << "12. Trier par prix decroissant\n";
        cout << "13. Trier par nom\n";
        cout << "14. Trier par quantite\n";
        cout << "15. Quitter\n";
        cout << "Choix : "; cin >> choix;

        switch (choix) {
            case 1: stock.rechercherParReference(); break;
            case 2: stock.ajouterArticle(); break;
            case 3: stock.afficherArticles(); break;
            case 4: stock.supprimerArticle(); break;
            case 5: stock.modifierArticle(); break;
            case 6: stock.rechercherParNom(); break;
            case 7: stock.rechercherParPrix(); break;
            case 8: stock.sauvegarder(); break;
            case 9: stock.exporterCSV(); break;
            case 10: stock.valeurTotale(); break;
            case 11: stock.trierParPrixCroissant(); break;
            case 12: stock.trierParPrixDecroissant(); break;
            case 13: stock.trierParNom(); break;
            case 14: stock.trierParQuantite(); break;
            case 15: cout << "Au revoir !\n"; break;
            default: cout << "Choix invalide.\n"; break;
        }
    } while (choix != 15);

    return 0;
}


/*
bool estVide() const {
    return articles.empty();
}
void afficherArticles() {
    if (estVide()) {
        cout << "❗ Le stock est vide.\n";
        return;
    }
    for (const auto& a : articles)
        cout << a.toString() << endl;
}




void valeurTotale() {
    double total = 0;

    for (const auto& a : articles)
        total += a.getPrix() * a.getQuantite();

    cout << "💰 Valeur totale du stock : " << total << " DH\n";
}






void exporterCSV() {
    ofstream f("exemple.csv");

    if (!f) {
        cout << " Impossible d’ouvrir stock.csv\n";
        return;
    }

    f << "Reference,Nom,Prix,Quantite\n";

    for (const auto& a : articles) {
        f << a.getReference() << ","
          << a.getNom() << ","
          << a.getPrix() << ","
          << a.getQuantite() << "\n";
    }

    f.close();
    cout << "Export réalisé dans D:\\stock.csv\n";
}




// ----- Comparateurs lisibles -----

static bool comparerPrixCroissant(const Article& a, const Article& b) {
    return a.getPrix() < b.getPrix();
}

static bool comparerPrixDecroissant(const Article& a, const Article& b) {
    return a.getPrix() > b.getPrix();
}

static bool comparerNom(const Article& a, const Article& b) {
    return a.getNom() < b.getNom();
}

static bool comparerQuantite(const Article& a, const Article& b) {
    return a.getQuantite() < b.getQuantite();
}




void trierParPrixCroissant() {
    sort(articles.begin(), articles.end(), comparerPrixCroissant);
    cout << " Tri par prix croissant effectué.\n";
}
void trierParPrixDecroissant() {
    sort(articles.begin(), articles.end(), comparerPrixDecroissant);
    cout << " Tri par prix décroissant effectué.\n";
}
void trierParNom() {
    sort(articles.begin(), articles.end(), comparerNom);
    cout << " Tri par nom effectué.\n";
}
void trierParQuantite() {
    sort(articles.begin(), articles.end(), comparerQuantite);
    cout << " Tri par quantité effectué.\n";
}




*/