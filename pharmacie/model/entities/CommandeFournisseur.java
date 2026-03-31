package model.entities;

import pharmacie.model.enums.StatutCommande;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CommandeFournisseur {
    private int idCommande;
    private LocalDate dateCommande;
    private LocalDate dateReception;
    private StatutCommande statut;
    private Fournisseur fournisseur;
    private List<LigneCommande> lignes;
    
    public CommandeFournisseur() {
        this.lignes = new ArrayList<>();
        this.dateCommande = LocalDate.now();
        this.statut = StatutCommande.EN_ATTENTE;
    }
    
    public CommandeFournisseur(Fournisseur fournisseur) {
        this();
        this.fournisseur = fournisseur;
    }
    
    // Getters et Setters
    public int getIdCommande() { return idCommande; }
    public void setIdCommande(int idCommande) { this.idCommande = idCommande; }
    
    public LocalDate getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDate dateCommande) { this.dateCommande = dateCommande; }
    
    public LocalDate getDateReception() { return dateReception; }
    public void setDateReception(LocalDate dateReception) { this.dateReception = dateReception; }
    
    public StatutCommande getStatut() { return statut; }
    public void setStatut(StatutCommande statut) { this.statut = statut; }
    
    public Fournisseur getFournisseur() { return fournisseur; }
    public void setFournisseur(Fournisseur fournisseur) { this.fournisseur = fournisseur; }
    
    public List<LigneCommande> getLignes() { return lignes; }
    public void setLignes(List<LigneCommande> lignes) { this.lignes = lignes; }
    
    // Méthodes métier
    public void ajouterLigne(Medicament medicament, int quantite, double prixUnitaire) {
        LigneCommande ligne = new LigneCommande();
        ligne.setMedicament(medicament);
        ligne.setQuantite(quantite);
        ligne.setPrixUnitaire(prixUnitaire);
        lignes.add(ligne);
    }
    
    public double calculerMontantTotal() {
        return lignes.stream()
            .mapToDouble(l -> l.getQuantite() * l.getPrixUnitaire())
            .sum();
    }
    
    public int getNombreArticles() {
        return lignes.stream()
            .mapToInt(LigneCommande::getQuantite)
            .sum();
    }
    
    public void valider() {
        this.statut = StatutCommande.VALIDE;
    }
    
    public void livrer() {
        this.statut = StatutCommande.LIVRE;
        this.dateReception = LocalDate.now();
    }
    
    public void annuler() {
        this.statut = StatutCommande.ANNULE;
    }
    
    @Override
    public String toString() {
        return "Commande #" + idCommande + " - " + fournisseur.getNom() + 
               " (" + statut + ")";
    }
}
