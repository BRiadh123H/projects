package pharmacie.model.enums;

public enum TypeUtilisateur {
    ADMIN("Administrateur"),
    EMPLOYE("Employé");
    
    private String libelle;
    
    TypeUtilisateur(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    @Override
    public String toString() {
        return libelle;
    }
}
