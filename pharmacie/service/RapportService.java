package pharmacie.service;

import pharmacie.dao.RapportDAO;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class RapportService {
    private RapportDAO rapportDAO;
    private GestionStockService gestionStockService;
    private GestionVenteService gestionVenteService;
    
    public RapportService() {
        this.rapportDAO = new RapportDAO();
        this.gestionStockService = new GestionStockService();
        this.gestionVenteService = new GestionVenteService();
    }
    
    // Statistiques générales
    public Map<String, Object> getStatistiquesGenerales(LocalDate dateDebut, LocalDate dateFin) throws SQLException {
        return rapportDAO.getStatistiquesVentes(dateDebut, dateFin);
    }
    
    // Rapports de vente
    public List<Map<String, Object>> getTopMedicamentsVendus(int limit, LocalDate dateDebut, LocalDate dateFin) throws SQLException {
        return rapportDAO.getTopMedicamentsVendus(limit, dateDebut, dateFin);
    }
    
    public List<Map<String, Object>> getEvolutionCAMensuel(int annee) throws SQLException {
        return rapportDAO.getEvolutionCAMensuel(annee);
    }
    
    // Rapports sur les fournisseurs
    public List<Map<String, Object>> getPerformanceFournisseurs(LocalDate dateDebut, LocalDate dateFin) throws SQLException {
        return rapportDAO.getPerformanceFournisseurs(dateDebut, dateFin);
    }
    
    // Rapports sur les clients
    public List<Map<String, Object>> getClientsFideles(int limit) throws SQLException {
        return rapportDAO.getClientsFideles(limit);
    }
    
    // Rapports de stock
    public List<Map<String, Object>> getAlertesStock() throws SQLException {
        return rapportDAO.getAlertesStock();
    }
    
    public double getValeurTotaleStock() throws SQLException {
        return gestionStockService.getValeurTotaleStock();
    }
    
    public int getNombreMedicamentsEnStock() throws SQLException {
        return gestionStockService.getNombreMedicamentsEnStock();
    }
    
    public int getNombreMedicamentsEnRupture() throws SQLException {
        return gestionStockService.getMedicamentsEnRupture().size();
    }
    
    // Générer un rapport complet
    public String genererRapportComplet() throws SQLException {
        StringBuilder rapport = new StringBuilder();
        LocalDate aujourdhui = LocalDate.now();
        LocalDate debutMois = aujourdhui.withDayOfMonth(1);
        
        rapport.append("RAPPORT COMPLET - ").append(aujourdhui).append("\n");
        rapport.append("=========================================\n\n");
        
        // Section Stock
        rapport.append("1. ÉTAT DU STOCK\n");
        rapport.append("----------------\n");
        rapport.append("Valeur totale du stock: ").append(String.format("%.2f", getValeurTotaleStock())).append(" €\n");
        rapport.append("Nombre de médicaments en stock: ").append(getNombreMedicamentsEnStock()).append("\n");
        rapport.append("Nombre de médicaments en rupture: ").append(getNombreMedicamentsEnRupture()).append("\n");
        
        List<Map<String, Object>> alertes = getAlertesStock();
        if (!alertes.isEmpty()) {
            rapport.append("\nAlertes de stock:\n");
            for (Map<String, Object> alerte : alertes) {
                rapport.append("- ").append(alerte.get("nom"))
                      .append(": ").append(alerte.get("quantiteStock"))
                      .append(" (seuil: ").append(alerte.get("seuilMinimum")).append(")\n");
            }
        }
        
        // Section Ventes
        rapport.append("\n2. STATISTIQUES DE VENTES\n");
        rapport.append("-------------------------\n");
        Map<String, Object> stats = getStatistiquesGenerales(debutMois, aujourdhui);
        rapport.append("Chiffre d'affaires du mois: ").append(String.format("%.2f", stats.get("chiffreAffaires"))).append(" €\n");
        rapport.append("Nombre de ventes: ").append(stats.get("nombreVentes")).append("\n");
        rapport.append("Panier moyen: ").append(String.format("%.2f", stats.get("panierMoyen"))).append(" €\n");
        
        // Top médicaments
        List<Map<String, Object>> topMedicaments = getTopMedicamentsVendus(5, debutMois, aujourdhui);
        if (!topMedicaments.isEmpty()) {
            rapport.append("\nTop 5 des médicaments vendus:\n");
            for (int i = 0; i < topMedicaments.size(); i++) {
                Map<String, Object> med = topMedicaments.get(i);
                rapport.append(i + 1).append(". ").append(med.get("nom"))
                      .append(" - ").append(med.get("quantiteVendue")).append(" unités\n");
            }
        }
        
        // Section Fournisseurs
        rapport.append("\n3. PERFORMANCE DES FOURNISSEURS\n");
        rapport.append("--------------------------------\n");
        List<Map<String, Object>> fournisseurs = getPerformanceFournisseurs(debutMois.minusMonths(3), aujourdhui);
        if (!fournisseurs.isEmpty()) {
            for (Map<String, Object> fournisseur : fournisseurs) {
                rapport.append("- ").append(fournisseur.get("nom"))
                      .append(": ").append(String.format("%.2f", fournisseur.get("montantTotal"))).append(" €")
                      .append(" (").append(fournisseur.get("nombreCommandes")).append(" commandes)\n");
            }
        }
        
        // Section Clients
        rapport.append("\n4. CLIENTS FIDÈLES\n");
        rapport.append("------------------\n");
        List<Map<String, Object>> clients = getClientsFideles(5);
        if (!clients.isEmpty()) {
            for (int i = 0; i < clients.size(); i++) {
                Map<String, Object> client = clients.get(i);
                rapport.append(i + 1).append(". ").append(client.get("nom")).append(" ").append(client.get("prenom"))
                      .append(" - ").append(String.format("%.2f", client.get("montantTotal"))).append(" €")
                      .append(" (").append(client.get("nombreAchats")).append(" achats)\n");
            }
        }
        
        return rapport.toString();
    }
}
