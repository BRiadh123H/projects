package pharmacie.model.entities;

public class LigneCommande {
    private int idLigne;
    private int quantite;
    private double prixUnitaire;
    private CommandeFournisseur commande;
    private Medicament medicament;
    
    public LigneCommande() {}
    
    public LigneCommande(Medicament medicament, int quantite, double prixUnitaire) {
        this.medicament = medicament;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }
    
    // Getters et Setters
    public int getIdLigne() { return idLigne; }
    public void setIdLigne(int idLigne) { this.idLigne = idLigne; }
    
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    
    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    
    public CommandeFournisseur getCommande() { return commande; }
    public void setCommande(CommandeFournisseur commande) { this.commande = commande; }
    
    public Medicament getMedicament() { return medicament; }
    public void setMedicament(Medicament medicament) { this.medicament = medicament; }
    
    public double getMontantTotal() {
        return quantite * prixUnitaire;
    }
}
