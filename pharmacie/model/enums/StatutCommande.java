package pharmacie.model.enums;

public enum StatutCommande {
    EN_ATTENTE("En attente"),
    VALIDE("Validée"),
    LIVRE("Livrée"),
    ANNULE("Annulée");
    
    private String libelle;
    
    StatutCommande(String libelle) {
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
