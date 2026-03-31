package pharmacie.model.entities;

import java.time.LocalDate;

public class Client {
    private int idClient;
    private String nom;
    private String prenom;
    private String telephone;
    private LocalDate datePremierAchat;
    
    public Client() {}
    
    public Client(String nom, String prenom, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.datePremierAchat = LocalDate.now();
    }
    
    // Getters et Setters
    public int getIdClient() { return idClient; }
    public void setIdClient(int idClient) { this.idClient = idClient; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public LocalDate getDatePremierAchat() { return datePremierAchat; }
    public void setDatePremierAchat(LocalDate datePremierAchat) { 
        this.datePremierAchat = datePremierAchat; 
    }
    
    public String getNomComplet() {
        return nom + " " + prenom;
    }
    
    @Override
    public String toString() {
        return getNomComplet() + " - " + telephone;
    }
}