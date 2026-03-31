package pharmacie.service;

import pharmacie.dao.CommandeDAO;
import pharmacie.dao.FournisseurDAO;
import pharmacie.dao.MedicamentDAO;
import pharmacie.model.entities.*;
import pharmacie.model.enums.StatutCommande;
import pharmacie.model.exceptions.CommandeNonValideException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class GestionCommandeService {
    private CommandeDAO commandeDAO;
    private FournisseurDAO fournisseurDAO;
    private MedicamentDAO medicamentDAO;
    private GestionStockService gestionStockService;
    
    public GestionCommandeService() {
        this.commandeDAO = new CommandeDAO();
        this.fournisseurDAO = new FournisseurDAO();
        this.medicamentDAO = new MedicamentDAO();
        this.gestionStockService = new GestionStockService();
    }
    
    // Gestion des commandes
    public void creerCommande(CommandeFournisseur commande) throws SQLException {
        commandeDAO.create(commande);
    }
    
    public CommandeFournisseur trouverCommandeParId(int id) throws SQLException {
        return commandeDAO.findById(id);
    }
    
    public List<CommandeFournisseur> listerToutesCommandes() throws SQLException {
        return commandeDAO.findAll();
    }
    
    public List<CommandeFournisseur> listerCommandesParFournisseur(int idFournisseur) throws SQLException {
        return commandeDAO.findByFournisseur(idFournisseur);
    }
    
    public List<CommandeFournisseur> listerCommandesParStatut(StatutCommande statut) throws SQLException {
        return commandeDAO.findByStatut(statut);
    }
    
    // Gestion du statut des commandes
    public void validerCommande(int idCommande) throws SQLException {
        commandeDAO.updateStatut(idCommande, StatutCommande.VALIDE);
    }
    
    public void livrerCommande(int idCommande) throws SQLException, CommandeNonValideException {
        CommandeFournisseur commande = commandeDAO.findById(idCommande);
        
        if (commande == null) {
            throw new CommandeNonValideException("Commande non trouvée", idCommande, "INEXISTANTE");
        }
        
        if (commande.getStatut() != StatutCommande.VALIDE) {
            throw new CommandeNonValideException(
                "Commande non valide pour livraison", 
                idCommande, 
                "STATUT: " + commande.getStatut()
            );
        }
        
        // Mettre à jour le stock pour chaque ligne de commande
        for (LigneCommande ligne : commande.getLignes()) {
            gestionStockService.augmenterStock(
                ligne.getMedicament().getIdMedicament(), 
                ligne.getQuantite()
            );
        }
        
        // Marquer la commande comme livrée
        commandeDAO.marquerCommeLivree(idCommande, LocalDate.now());
    }
    
    public void annulerCommande(int idCommande) throws SQLException {
        commandeDAO.updateStatut(idCommande, StatutCommande.ANNULE);
    }
    
    // Gestion des fournisseurs
    public void ajouterFournisseur(Fournisseur fournisseur) throws SQLException {
        fournisseurDAO.create(fournisseur);
    }
    
    public void modifierFournisseur(Fournisseur fournisseur) throws SQLException {
        fournisseurDAO.update(fournisseur);
    }
    
    public void supprimerFournisseur(int idFournisseur) throws SQLException {
        fournisseurDAO.delete(idFournisseur);
    }
    
    public Fournisseur trouverFournisseurParId(int id) throws SQLException {
        return fournisseurDAO.findById(id);
    }
    
    public List<Fournisseur> listerTousFournisseurs() throws SQLException {
        return fournisseurDAO.findAll();
    }
    
    public List<Fournisseur> rechercherFournisseurs(String terme) throws SQLException {
        return fournisseurDAO.searchByNom(terme);
    }
    
    // Générer une commande basée sur les alertes de stock
    public CommandeFournisseur genererCommandeAutomatique(Fournisseur fournisseur) throws SQLException {
        CommandeFournisseur commande = new CommandeFournisseur(fournisseur);
        
        // Trouver les médicaments qui ont besoin d'être réapprovisionnés
        List<Medicament> medicaments = gestionStockService.getMedicamentsSeuilAtteint();
        
        for (Medicament medicament : medicaments) {
            // Calculer la quantité à commander (seuil minimum * 2)
            int quantiteACommander = medicament.getSeuilMinimum() * 2;
            commande.ajouterLigne(medicament, quantiteACommander, medicament.getPrix() * 0.7); // Prix d'achat à 70% du prix de vente
        }
        
        return commande;
    }
}
