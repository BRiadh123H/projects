package pharmacie.model.entities;

public class LigneVente {
    private int idLigne;
    private int quantite;
    private double prixUnitaire;
    private Vente vente;
    private Medicament medicament;
    
    public LigneVente() {}
    
    public LigneVente(Medicament medicament, int quantite) {
        this.medicament = medicament;
        this.quantite = quantite;
        this.prixUnitaire = medicament.getPrix();
    }
    
    // Getters et Setters
    public int getIdLigne() { return idLigne; }
    public void setIdLigne(int idLigne) { this.idLigne = idLigne; }
    
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    
    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    
    public Vente getVente() { return vente; }
    public void setVente(Vente vente) { this.vente = vente; }
    
    public Medicament getMedicament() { return medicament; }
    public void setMedicament(Medicament medicament) { this.medicament = medicament; }
    
    public double getMontantTotal() {
        return quantite * prixUnitaire;
    }
    
    @Override
    public String toString() {
        return medicament.getNom() + " x" + quantite + " = " + getMontantTotal() + " €";
    }
}
