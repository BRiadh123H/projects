package pharmacie.model.exceptions;

public class StockInsuffisantException extends Exception {
    private String nomMedicament;
    private int quantiteDemandee;
    private int quantiteDisponible;
    
    public StockInsuffisantException(String message) {
        super(message);
    }
    
    public StockInsuffisantException(String message, String nomMedicament, 
                                     int quantiteDemandee, int quantiteDisponible) {
        super(message);
        this.nomMedicament = nomMedicament;
        this.quantiteDemandee = quantiteDemandee;
        this.quantiteDisponible = quantiteDisponible;
    }
    
    // Getters
    public String getNomMedicament() { return nomMedicament; }
    public int getQuantiteDemandee() { return quantiteDemandee; }
    public int getQuantiteDisponible() { return quantiteDisponible; }
    
    @Override
    public String getMessage() {
        if (nomMedicament != null) {
            return super.getMessage() + 
                   " Médicament: " + nomMedicament + 
                   ", Demandé: " + quantiteDemandee + 
                   ", Disponible: " + quantiteDisponible;
        }
        return super.getMessage();
    }
}
