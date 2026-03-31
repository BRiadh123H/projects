package pharmacie.model.exceptions;

public class CommandeNonValideException extends Exception {
    private int idCommande;
    private String raison;
    
    public CommandeNonValideException(String message) {
        super(message);
    }
    
    public CommandeNonValideException(String message, int idCommande, String raison) {
        super(message);
        this.idCommande = idCommande;
        this.raison = raison;
    }
    
    // Getters
    public int getIdCommande() { return idCommande; }
    public String getRaison() { return raison; }
    
    @Override
    public String getMessage() {
        if (idCommande > 0) {
            return super.getMessage() + 
                   " Commande #" + idCommande + 
                   ", Raison: " + raison;
        }
        return super.getMessage();
    }
}
