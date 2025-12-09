#include <bits/stdc++.h>
using namespace std;

int main() {

    cout << "===== CREATION DU VECTOR =====\n";
    vector<string> noms;
    noms.reserve(10);   // reserve space but size = 0

    noms.push_back("Mohamed");
    noms.push_back("Ahmad");
    noms.push_back("Ali");
    noms.push_back("Douaa");

    cout << "Contenu initial :\n";
    for (const auto& nom : noms)
        cout << " - " << nom << "\n";

    cout << "\nTaille = " << noms.size()
         << "  |  Capacite = " << noms.capacity() << "\n\n";


    cout << "===== INSERTION =====\n";

    auto it = noms.begin();
    noms.insert(it, "Riadh");            // insert at beginning

    cout << "Apres insertion de 'Riadh' au debut :\n";
    for (const auto& nom : noms) cout << nom << " ";
    cout << "\n";

    noms.insert(noms.begin() + 2, "Karem");   // insert in the middle

    cout << "Apres insertion de 'Karem' a l'indice 2 :\n";
    for (const auto& nom : noms) cout << nom << " ";
    cout << "\n\n";


    cout << "===== SUPPRESSION D'UNE PLAGE =====\n";

    auto debut = noms.begin() + 1;
    auto fin   = noms.begin() + 3;
    noms.erase(debut, fin);   // erase index 1 and 2

    cout << "Apres effacement des positions 1 a 2 :\n";
    for (const auto& nom : noms) cout << nom << " ";
    cout << "\n\n";


    cout << "===== TRI & REVERSE =====\n";

    sort(noms.begin(), noms.end());
    cout << "Trie croissant : ";
    for (auto& nom : noms) cout << nom << " ";
    cout << "\n";

    reverse(noms.begin(), noms.end());
    cout << "Trie decroissant : ";
    for (auto& nom : noms) cout << nom << " ";
    cout << "\n\n";


    cout << "===== RECHERCHE =====\n";

    string cible = "Ali";
    auto pos = find(noms.begin(), noms.end(), cible);

    if (pos != noms.end())
        cout << cible << " trouve a l'indice "
             << distance(noms.begin(), pos) << "\n";
    else
        cout << cible << " n'existe pas dans le vector.\n";

    cout << "\n===== MODIFICATION D'UN ELEMENT =====\n";
    noms[0] = "*** " + noms[0] + " ***";

    cout << "Contenu final :\n";
    for (const auto& nom : noms) cout << nom << "\n";


    cout << "\nProgramme termine.\n";
    return 0;
}
