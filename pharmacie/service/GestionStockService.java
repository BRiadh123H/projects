package pharmacie.service;

import pharmacie.dao.MedicamentDAO;
import pharmacie.model.entities.Medicament;
import pharmacie.model.exceptions.SeuilMinimumAtteintException;
import pharmacie.model.exceptions.StockInsuffisantException;
import java.sql.SQLException;
import java.util.List;

public class GestionStockService {
    private MedicamentDAO medicamentDAO;
    
    public GestionStockService() {
        this.medicamentDAO = new MedicamentDAO();
    }
    
    // CRUD Médicaments
    public void ajouterMedicament(Medicament medicament) throws SQLException {
        medicamentDAO.create(medicament);
    }
    
    public void modifierMedicament(Medicament medicament) throws SQLException {
        medicamentDAO.update(medicament);
    }
    
    public void supprimerMedicament(int idMedicament) throws SQLException {
        medicamentDAO.delete(idMedicament);
    }
    
    public Medicament trouverMedicamentParId(int id) throws SQLException {
        return medicamentDAO.findById(id);
    }
    
    public List<Medicament> listerTousMedicaments() throws SQLException {
        return medicamentDAO.findAll();
    }
    
    // Gestion du stock
    public void augmenterStock(int idMedicament, int quantite) throws SQLException {
        medicamentDAO.updateStock(idMedicament, quantite);
    }
    
    public void diminuerStock(int idMedicament, int quantite) throws SQLException, StockInsuffisantException {
        Medicament medicament = medicamentDAO.findById(idMedicament);
        if (medicament == null) {
            throw new SQLException("Médicament non trouvé");
        }
        
        if (medicament.getQuantiteStock() < quantite) {
            throw new StockInsuffisantException(
                "Stock insuffisant pour " + medicament.getNom(),
                medicament.getNom(),
                quantite,
                medicament.getQuantiteStock()
            );
        }
        
        medicamentDAO.updateStock(idMedicament, -quantite);
    }
    
    // Alertes et surveillance
    public List<Medicament> getMedicamentsEnRupture() throws SQLException {
        return medicamentDAO.findMedicamentsEnRupture();
    }
    
    public List<Medicament> getMedicamentsSeuilAtteint() throws SQLException, SeuilMinimumAtteintException {
        return medicamentDAO.findMedicamentsSeuilAtteint();
    }
    
    public void verifierAlertesStock() throws SQLException, SeuilMinimumAtteintException {
        List<Medicament> medicamentsSeuil = getMedicamentsSeuilAtteint();
        if (!medicamentsSeuil.isEmpty()) {
            StringBuilder message = new StringBuilder("Alertes de stock:\n");
            for (Medicament m : medicamentsSeuil) {
                message.append("- ").append(m.getNom())
                      .append(": ").append(m.getQuantiteStock())
                      .append("/").append(m.getSeuilMinimum()).append("\n");
            }
            throw new SeuilMinimumAtteintException(message.toString());
        }
    }
    
    // Recherche
    public List<Medicament> rechercherMedicaments(String terme) throws SQLException {
        return medicamentDAO.searchByNom(terme);
    }
    
    // Statistiques
    public double getValeurTotaleStock() throws SQLException {
        return medicamentDAO.getValeurTotaleStock();
    }
    
    public int getNombreMedicamentsEnStock() throws SQLException {
        List<Medicament> medicaments = medicamentDAO.findAll();
        return medicaments.size();
    }
}
