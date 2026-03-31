package model.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Vente {
    private int idVente;
    private LocalDateTime dateVente;
    private double montantTotal;
    private Client client;
    private Utilisateur utilisateur;
    private List<LigneVente> lignes;
    
    public Vente() {
        this.lignes = new ArrayList<>();
        this.dateVente = LocalDateTime.now();
    }
    
    public Vente(Client client, Utilisateur utilisateur) {
        this();
        this.client = client;
        this.utilisateur = utilisateur;
    }
    
    // Getters et Setters
    public int getIdVente() { return idVente; }
    public void setIdVente(int idVente) { this.idVente = idVente; }
    
    public LocalDateTime getDateVente() { return dateVente; }
    public void setDateVente(LocalDateTime dateVente) { this.dateVente = dateVente; }
    
    public double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(double montantTotal) { this.montantTotal = montantTotal; }
    
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
    
    public List<LigneVente> getLignes() { return lignes; }
    public void setLignes(List<LigneVente> lignes) { this.lignes = lignes; }
    
    // Méthodes métier
    public void ajouterLigne(Medicament medicament, int quantite) {
        LigneVente ligne = new LigneVente();
        ligne.setMedicament(medicament);
        ligne.setQuantite(quantite);
        ligne.setPrixUnitaire(medicament.getPrix());
        lignes.add(ligne);
        
        // Mettre à jour le montant total
        recalculerMontantTotal();
    }
    
    private void recalculerMontantTotal() {
        this.montantTotal = lignes.stream()
            .mapToDouble(l -> l.getQuantite() * l.getPrixUnitaire())
            .sum();
    }
    
    public int getNombreArticles() {
        return lignes.stream()
            .mapToInt(LigneVente::getQuantite)
            .sum();
    }
    
    @Override
    public String toString() {
        return "Vente #" + idVente + " - " + dateVente.toLocalDate() + 
               " - " + montantTotal + " €";
    }
}