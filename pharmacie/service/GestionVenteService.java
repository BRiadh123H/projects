package pharmacie.service;

import pharmacie.dao.VenteDAO;
import pharmacie.model.entities.*;
import pharmacie.model.exceptions.StockInsuffisantException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class GestionVenteService {
    private VenteDAO venteDAO;
    private GestionStockService gestionStockService;
    
    public GestionVenteService() {
        this.venteDAO = new VenteDAO();
        this.gestionStockService = new GestionStockService();
    }
    
    // Enregistrer une vente
    public Vente enregistrerVente(Vente vente) throws SQLException, StockInsuffisantException {
        // Vérifier le stock pour chaque ligne avant de procéder
        for (LigneVente ligne : vente.getLignes()) {
            Medicament medicament = ligne.getMedicament();
            if (medicament.getQuantiteStock() < ligne.getQuantite()) {
                throw new StockInsuffisantException(
                    "Stock insuffisant pour " + medicament.getNom(),
                    medicament.getNom(),
                    ligne.getQuantite(),
                    medicament.getQuantiteStock()
                );
            }
        }
        
        // Enregistrer la vente (cela mettra à jour le stock automatiquement via le DAO)
        venteDAO.create(vente);
        return vente;
    }
    
    // Rechercher des ventes
    public Vente trouverVenteParId(int id) throws SQLException {
        return venteDAO.findById(id);
    }
    
    public List<Vente> listerToutesVentes() throws SQLException {
        return venteDAO.findAll();
    }
    
    public List<Vente> listerVentesParDate(LocalDate date) throws SQLException {
        return venteDAO.findByDate(date);
    }
    
    public List<Vente> listerVentesParClient(int idClient) throws SQLException {
        return venteDAO.findByClient(idClient);
    }
    
    public List<Vente> listerVentesParUtilisateur(int idUtilisateur) throws SQLException {
        return venteDAO.findByUtilisateur(idUtilisateur);
    }
    
    // Chiffre d'affaires
    public double getChiffreAffairesJournalier(LocalDate date) throws SQLException {
        return venteDAO.getChiffreAffairesJournalier(date);
    }
    
    public double getChiffreAffairesMensuel(int mois, int annee) throws SQLException {
        return venteDAO.getChiffreAffairesMensuel(mois, annee);
    }
    
    public double getChiffreAffairesDateADate(LocalDate dateDebut, LocalDate dateFin) throws SQLException {
        double total = 0;
        List<Vente> ventes = listerToutesVentes();
        
        for (Vente vente : ventes) {
            LocalDate dateVente = vente.getDateVente().toLocalDate();
            if (!dateVente.isBefore(dateDebut) && !dateVente.isAfter(dateFin)) {
                total += vente.getMontantTotal();
            }
        }
        
        return total;
    }
    
    // Générer un ticket de vente
    public String genererTicket(Vente vente) {
        StringBuilder ticket = new StringBuilder();
        ticket.append("================================\n");
        ticket.append("       PHARMACIE MONDIALE\n");
        ticket.append("================================\n");
        ticket.append("Date: ").append(vente.getDateVente()).append("\n");
        ticket.append("Vente #").append(vente.getIdVente()).append("\n");
        
        if (vente.getClient() != null) {
            ticket.append("Client: ").append(vente.getClient().getNomComplet()).append("\n");
        }
        
        ticket.append("Caissier: ").append(vente.getUtilisateur().getNomComplet()).append("\n");
        ticket.append("--------------------------------\n");
        
        for (LigneVente ligne : vente.getLignes()) {
            Medicament medicament = ligne.getMedicament();
            ticket.append(String.format("%-20s %3d x %6.2f = %7.2f €\n",
                medicament.getNom(),
                ligne.getQuantite(),
                ligne.getPrixUnitaire(),
                ligne.getMontantTotal()));
        }
        
        ticket.append("--------------------------------\n");
        ticket.append(String.format("TOTAL: %27.2f €\n", vente.getMontantTotal()));
        ticket.append("================================\n");
        ticket.append("Merci de votre visite !\n");
        
        return ticket.toString();
    }
}