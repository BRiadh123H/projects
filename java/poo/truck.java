package poo;

public class truck extends vehicle implements reparable {
    int capaciteCharge;

    public truck() {
        super();
        this.capaciteCharge = 0;
    }

    public truck(String marque, String modele, int annee, int capaciteCharge) {
        super(marque, modele, annee);
        this.capaciteCharge = capaciteCharge;
    }
    public void repare() {
    System.out.println("Réparation du camion " + marque);
}

    
    
    
    void demarrer() {
        System.out.println("Le camion " + marque + " démarre.");
    }

    void afficherInfos() {
        System.out.println("Camion " + marque + " roule à " + getVitesse() + " km/h avec une capacité de charge de " + capaciteCharge + " kg");
    }
}


