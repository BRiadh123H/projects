package pharmacie.model.entities;

import pharmacie.model.enums.TypeUtilisateur;

public class Utilisateur {
    private int idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String login;
    private String motDePasse;
    private TypeUtilisateur type;
    
    public Utilisateur() {}
    
    public Utilisateur(String nom, String prenom, String email, String login, 
                      String motDePasse, TypeUtilisateur type) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.login = login;
        this.motDePasse = motDePasse;
        this.type = type;
    }
    
    // Getters et Setters
    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    
    public TypeUtilisateur getType() { return type; }
    public void setType(TypeUtilisateur type) { this.type = type; }
    
    @Override
    public String toString() {
        return nom + " " + prenom + " (" + type + ")";
    }
}