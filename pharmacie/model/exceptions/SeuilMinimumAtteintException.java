package pharmacie.model.exceptions;

public class SeuilMinimumAtteintException extends Exception {
    private String nomMedicament;
    private int quantiteStock;
    private int seuilMinimum;
    
    public SeuilMinimumAtteintException(String message) {
        super(message);
    }
    
    public SeuilMinimumAtteintException(String message, String nomMedicament, 
                                       int quantiteStock, int seuilMinimum) {
        super(message);
        this.nomMedicament = nomMedicament;
        this.quantiteStock = quantiteStock;
        this.seuilMinimum = seuilMinimum;
    }
    
    // Getters
    public String getNomMedicament() { return nomMedicament; }
    public int getQuantiteStock() { return quantiteStock; }
    public int getSeuilMinimum() { return seuilMinimum; }
    
    @Override
    public String getMessage() {
        if (nomMedicament != null) {
            return super.getMessage() + 
                   " Médicament: " + nomMedicament + 
                   ", Stock: " + quantiteStock + 
                   ", Seuil: " + seuilMinimum;
        }
        return super.getMessage();
    }
}
