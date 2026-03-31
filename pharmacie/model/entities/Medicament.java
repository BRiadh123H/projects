package pharmacie.model.entities;

public class Medicament {
    private int idMedicament;
    private String nom;
    private String dosage;
    private String forme;
    private String categorie;
    private double prix;
    private int quantiteStock;
    private int seuilMinimum;
    
    public Medicament() {}
    
    public Medicament(String nom, String dosage, String forme, String categorie, 
                     double prix, int seuilMinimum) {
        this.nom = nom;
        this.dosage = dosage;
        this.forme = forme;
        this.categorie = categorie;
        this.prix = prix;
        this.seuilMinimum = seuilMinimum;
        this.quantiteStock = 0;
    }
    
    // Getters et Setters
    public int getIdMedicament() { return idMedicament; }
    public void setIdMedicament(int idMedicament) { this.idMedicament = idMedicament; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    
    public String getForme() { return forme; }
    public void setForme(String forme) { this.forme = forme; }
    
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    
    public int getQuantiteStock() { return quantiteStock; }
    public void setQuantiteStock(int quantiteStock) { this.quantiteStock = quantiteStock; }
    
    public int getSeuilMinimum() { return seuilMinimum; }
    public void setSeuilMinimum(int seuilMinimum) { this.seuilMinimum = seuilMinimum; }
    
    // Méthodes métier
    public void diminuerStock(int quantite) {
        if (quantite <= quantiteStock) {
            quantiteStock -= quantite;
        }
    }
    
    public void augmenterStock(int quantite) {
        quantiteStock += quantite;
    }
    
    public boolean estEnRupture() {
        return quantiteStock <= 0;
    }
    
    public boolean atteintSeuilMinimum() {
        return quantiteStock <= seuilMinimum;
    }
    
    public double calculerValeurStock() {
        return quantiteStock * prix;
    }
    
    @Override
    public String toString() {
        return nom + " " + dosage + " - " + forme + " (" + quantiteStock + " unités)";
    }
}
