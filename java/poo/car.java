package poo1;

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

    void afficherInfos() {
        System.out.println("Voiture " + marque + " roule à " + getVitesse() + " km/h");
    }
    
}
