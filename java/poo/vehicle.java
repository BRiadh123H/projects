package poo;

public abstract class vehicle {
    protected String marque;
    protected String modele;
    protected int annee;

    private int vitesse;
    
    public int getVitesse() {
        return vitesse;
    }
    
    public void setVitesse(int vitesse) throws invalidspeedexception { 
        if (vitesse <0){
            throw new invalidspeedexception("La vitesse ne peut pas être négative !");
        }
        this.vitesse = vitesse;
    }
    
    public vehicle() {
        this.marque = "Inconnu";
        this.modele = "Inconnu";
        this.annee = 0;
        this.vitesse = 0;
    }

    public vehicle(String marque, String modele, int annee) {
        this.marque = marque;
        this.modele = modele;
        this.annee = annee;
        this.vitesse = 0;
    }

    void accelerer() { vitesse += 10; }

    void freiner() { 
        if (vitesse >= 10) {
            vitesse -= 10; 
        }
    }

    abstract void demarrer();

    void arreter() { vitesse = 0; }

    void afficherInfos() {
        System.out.println("Véhicule " + marque + " roule à " + vitesse + " km/h");
    }
}



