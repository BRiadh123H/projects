package poo;

public class car extends vehicle {
    public car() {
        super();
    }
    public car(String marque, String modele, int annee) {
        super(marque, modele, annee);
    }

    @Override
    void demarrer() {
        System.out.println("La voiture " + marque + " démarre.");
    }

    @Override // Ajout de l'annotation
    void afficherInfos() {
        System.out.println("Voiture " + marque + " roule à " + getVitesse() + " km/h");
    }
    
}
