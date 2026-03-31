package pharmacie.dao;

import pharmacie.model.entities.CommandeFournisseur;
import pharmacie.model.entities.Fournisseur;
import pharmacie.model.entities.LigneCommande;
import pharmacie.model.entities.Medicament;
import pharmacie.model.enums.StatutCommande;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO extends BaseDAO {
    
    public void create(CommandeFournisseur commande) throws SQLException {
        Connection conn = null;
        PreparedStatement stmtCommande = null;
        PreparedStatement stmtLigne = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // Insert commande
            String sqlCommande = "INSERT INTO commande_fournisseur (date_commande, date_reception, statut, id_fournisseur) " +
                                "VALUES (?, ?, ?, ?)";
            stmtCommande = conn.prepareStatement(sqlCommande, Statement.RETURN_GENERATED_KEYS);
            stmtCommande.setDate(1, Date.valueOf(commande.getDateCommande()));
            if (commande.getDateReception() != null) {
                stmtCommande.setDate(2, Date.valueOf(commande.getDateReception()));
            } else {
                stmtCommande.setNull(2, Types.DATE);
            }
            stmtCommande.setString(3, commande.getStatut().name());
            stmtCommande.setInt(4, commande.getFournisseur().getIdFournisseur());
            
            stmtCommande.executeUpdate();
            
            rs = stmtCommande.getGeneratedKeys();
            if (rs.next()) {
                commande.setIdCommande(rs.getInt(1));
            }
            
            // Insert lignes commande
            String sqlLigne = "INSERT INTO ligne_commande (quantite, prix_unitaire, id_commande, id_medicament) " +
                             "VALUES (?, ?, ?, ?)";
            stmtLigne = conn.prepareStatement(sqlLigne);
            
            for (LigneCommande ligne : commande.getLignes()) {
                stmtLigne.setInt(1, ligne.getQuantite());
                stmtLigne.setDouble(2, ligne.getPrixUnitaire());
                stmtLigne.setInt(3, commande.getIdCommande());
                stmtLigne.setInt(4, ligne.getMedicament().getIdMedicament());
                stmtLigne.addBatch();
            }
            
            stmtLigne.executeBatch();
            
            conn.commit();
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erreur lors du rollback: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (stmtLigne != null) stmtLigne.close();
            closeResources(conn, stmtCommande, rs);
        }
    }
    
    public CommandeFournisseur findById(int id) throws SQLException {
        String sql = "SELECT c.*, f.* FROM commande_fournisseur c " +
                    "JOIN fournisseur f ON c.id_fournisseur = f.id_fournisseur " +
                    "WHERE c.id_commande = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CommandeFournisseur commande = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                commande = mapResultSetToCommande(rs);
                
                // Charger les lignes de commande
                commande.setLignes(findLignesByCommandeId(id, conn));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return commande;
    }
    
    public List<CommandeFournisseur> findAll() throws SQLException {
        String sql = "SELECT c.*, f.* FROM commande_fournisseur c " +
                    "JOIN fournisseur f ON c.id_fournisseur = f.id_fournisseur " +
                    "ORDER BY c.date_commande DESC";
        List<CommandeFournisseur> commandes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                CommandeFournisseur commande = mapResultSetToCommande(rs);
                commandes.add(commande);
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return commandes;
    }
    
    public void updateStatut(int idCommande, StatutCommande statut) throws SQLException {
        String sql = "UPDATE commande_fournisseur SET statut = ? WHERE id_commande = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, statut.name());
            stmt.setInt(2, idCommande);
            stmt.executeUpdate();
        } finally {
            closeResources(conn, stmt);
        }
    }
    
    public void marquerCommeLivree(int idCommande, LocalDate dateLivraison) throws SQLException {
        String sql = "UPDATE commande_fournisseur SET statut = ?, date_reception = ? WHERE id_commande = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, StatutCommande.LIVRE.name());
            stmt.setDate(2, Date.valueOf(dateLivraison));
            stmt.setInt(3, idCommande);
            stmt.executeUpdate();
        } finally {
            closeResources(conn, stmt);
        }
    }
    
    public List<CommandeFournisseur> findByFournisseur(int idFournisseur) throws SQLException {
        String sql = "SELECT c.*, f.* FROM commande_fournisseur c " +
                    "JOIN fournisseur f ON c.id_fournisseur = f.id_fournisseur " +
                    "WHERE c.id_fournisseur = ? ORDER BY c.date_commande DESC";
        List<CommandeFournisseur> commandes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idFournisseur);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                commandes.add(mapResultSetToCommande(rs));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return commandes;
    }
    
    public List<CommandeFournisseur> findByStatut(StatutCommande statut) throws SQLException {
        String sql = "SELECT c.*, f.* FROM commande_fournisseur c " +
                    "JOIN fournisseur f ON c.id_fournisseur = f.id_fournisseur " +
                    "WHERE c.statut = ? ORDER BY c.date_commande DESC";
        List<CommandeFournisseur> commandes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, statut.name());
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                commandes.add(mapResultSetToCommande(rs));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return commandes;
    }
    
    private CommandeFournisseur mapResultSetToCommande(ResultSet rs) throws SQLException {
        CommandeFournisseur commande = new CommandeFournisseur();
        commande.setIdCommande(rs.getInt("id_commande"));
        
        Date dateCommande = rs.getDate("date_commande");
        if (dateCommande != null) {
            commande.setDateCommande(dateCommande.toLocalDate());
        }
        
        Date dateReception = rs.getDate("date_reception");
        if (dateReception != null) {
            commande.setDateReception(dateReception.toLocalDate());
        }
        
        commande.setStatut(StatutCommande.valueOf(rs.getString("statut")));
        
        // Créer et configurer le fournisseur
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setIdFournisseur(rs.getInt("id_fournisseur"));
        fournisseur.setNom(rs.getString("nom"));
        fournisseur.setAdresse(rs.getString("adresse"));
        fournisseur.setTelephone(rs.getString("telephone"));
        fournisseur.setEmail(rs.getString("email"));
        commande.setFournisseur(fournisseur);
        
        return commande;
    }
    
    private List<LigneCommande> findLignesByCommandeId(int idCommande, Connection conn) throws SQLException {
        List<LigneCommande> lignes = new ArrayList<>();
        String sql = "SELECT lc.*, m.* FROM ligne_commande lc " +
                    "JOIN medicament m ON lc.id_medicament = m.id_medicament " +
                    "WHERE lc.id_commande = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCommande);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                LigneCommande ligne = new LigneCommande();
                ligne.setIdLigne(rs.getInt("id_ligne"));
                ligne.setQuantite(rs.getInt("quantite"));
                ligne.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                
                // Créer et configurer le médicament
                Medicament medicament = new Medicament();
                medicament.setIdMedicament(rs.getInt("id_medicament"));
                medicament.setNom(rs.getString("nom"));
                medicament.setDosage(rs.getString("dosage"));
                medicament.setForme(rs.getString("forme"));
                medicament.setCategorie(rs.getString("categorie"));
                medicament.setPrix(rs.getDouble("prix"));
                medicament.setQuantiteStock(rs.getInt("quantite_stock"));
                medicament.setSeuilMinimum(rs.getInt("seuil_minimum"));
                ligne.setMedicament(medicament);
                
                lignes.add(ligne);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        
        return lignes;
    }
}
