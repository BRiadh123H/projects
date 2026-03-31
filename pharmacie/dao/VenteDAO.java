package pharmacie.dao;

import pharmacie.model.entities.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VenteDAO extends BaseDAO {
    
    public void create(Vente vente) throws SQLException {
        Connection conn = null;
        PreparedStatement stmtVente = null;
        PreparedStatement stmtLigne = null;
        PreparedStatement stmtUpdateStock = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // Insert vente
            String sqlVente = "INSERT INTO vente (date_vente, montant_total, id_client, id_utilisateur) " +
                             "VALUES (?, ?, ?, ?)";
            stmtVente = conn.prepareStatement(sqlVente, Statement.RETURN_GENERATED_KEYS);
            stmtVente.setTimestamp(1, Timestamp.valueOf(vente.getDateVente()));
            stmtVente.setDouble(2, vente.getMontantTotal());
            
            if (vente.getClient() != null) {
                stmtVente.setInt(3, vente.getClient().getIdClient());
            } else {
                stmtVente.setNull(3, Types.INTEGER);
            }
            
            stmtVente.setInt(4, vente.getUtilisateur().getIdUtilisateur());
            stmtVente.executeUpdate();
            
            rs = stmtVente.getGeneratedKeys();
            if (rs.next()) {
                vente.setIdVente(rs.getInt(1));
            }
            
            // Insert lignes vente et mettre à jour le stock
            String sqlLigne = "INSERT INTO ligne_vente (quantite, prix_unitaire, id_vente, id_medicament) " +
                             "VALUES (?, ?, ?, ?)";
            String sqlUpdateStock = "UPDATE medicament SET quantite_stock = quantite_stock - ? WHERE id_medicament = ?";
            
            stmtLigne = conn.prepareStatement(sqlLigne);
            stmtUpdateStock = conn.prepareStatement(sqlUpdateStock);
            
            for (LigneVente ligne : vente.getLignes()) {
                // Insert ligne vente
                stmtLigne.setInt(1, ligne.getQuantite());
                stmtLigne.setDouble(2, ligne.getPrixUnitaire());
                stmtLigne.setInt(3, vente.getIdVente());
                stmtLigne.setInt(4, ligne.getMedicament().getIdMedicament());
                stmtLigne.addBatch();
                
                // Mettre à jour le stock
                stmtUpdateStock.setInt(1, ligne.getQuantite());
                stmtUpdateStock.setInt(2, ligne.getMedicament().getIdMedicament());
                stmtUpdateStock.addBatch();
            }
            
            stmtLigne.executeBatch();
            stmtUpdateStock.executeBatch();
            
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
            if (stmtUpdateStock != null) stmtUpdateStock.close();
            closeResources(conn, stmtVente, rs);
        }
    }
    
    public Vente findById(int id) throws SQLException {
        String sql = "SELECT v.*, c.*, u.* FROM vente v " +
                    "LEFT JOIN client c ON v.id_client = c.id_client " +
                    "JOIN utilisateur u ON v.id_utilisateur = u.id_utilisateur " +
                    "WHERE v.id_vente = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vente vente = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                vente = mapResultSetToVente(rs);
                
                // Charger les lignes de vente
                vente.setLignes(findLignesByVenteId(id, conn));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return vente;
    }
    
    public List<Vente> findAll() throws SQLException {
        String sql = "SELECT v.*, c.*, u.* FROM vente v " +
                    "LEFT JOIN client c ON v.id_client = c.id_client " +
                    "JOIN utilisateur u ON v.id_utilisateur = u.id_utilisateur " +
                    "ORDER BY v.date_vente DESC";
        List<Vente> ventes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                ventes.add(mapResultSetToVente(rs));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return ventes;
    }
    
    public List<Vente> findByDate(LocalDate date) throws SQLException {
        String sql = "SELECT v.*, c.*, u.* FROM vente v " +
                    "LEFT JOIN client c ON v.id_client = c.id_client " +
                    "JOIN utilisateur u ON v.id_utilisateur = u.id_utilisateur " +
                    "WHERE DATE(v.date_vente) = ? ORDER BY v.date_vente DESC";
        List<Vente> ventes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(date));
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                ventes.add(mapResultSetToVente(rs));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return ventes;
    }
    
    public List<Vente> findByClient(int idClient) throws SQLException {
        String sql = "SELECT v.*, c.*, u.* FROM vente v " +
                    "JOIN client c ON v.id_client = c.id_client " +
                    "JOIN utilisateur u ON v.id_utilisateur = u.id_utilisateur " +
                    "WHERE v.id_client = ? ORDER BY v.date_vente DESC";
        List<Vente> ventes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idClient);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                ventes.add(mapResultSetToVente(rs));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return ventes;
    }
    
    public List<Vente> findByUtilisateur(int idUtilisateur) throws SQLException {
        String sql = "SELECT v.*, c.*, u.* FROM vente v " +
                    "LEFT JOIN client c ON v.id_client = c.id_client " +
                    "JOIN utilisateur u ON v.id_utilisateur = u.id_utilisateur " +
                    "WHERE v.id_utilisateur = ? ORDER BY v.date_vente DESC";
        List<Vente> ventes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUtilisateur);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                ventes.add(mapResultSetToVente(rs));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return ventes;
    }
    
    public double getChiffreAffairesJournalier(LocalDate date) throws SQLException {
        String sql = "SELECT COALESCE(SUM(montant_total), 0) as ca FROM vente WHERE DATE(date_vente) = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        double chiffreAffaires = 0;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(date));
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                chiffreAffaires = rs.getDouble("ca");
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return chiffreAffaires;
    }
    
    public double getChiffreAffairesMensuel(int mois, int annee) throws SQLException {
        String sql = "SELECT COALESCE(SUM(montant_total), 0) as ca FROM vente " +
                    "WHERE MONTH(date_vente) = ? AND YEAR(date_vente) = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        double chiffreAffaires = 0;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, mois);
            stmt.setInt(2, annee);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                chiffreAffaires = rs.getDouble("ca");
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return chiffreAffaires;
    }
    
    private Vente mapResultSetToVente(ResultSet rs) throws SQLException {
        Vente vente = new Vente();
        vente.setIdVente(rs.getInt("id_vente"));
        
        Timestamp dateVente = rs.getTimestamp("date_vente");
        if (dateVente != null) {
            vente.setDateVente(dateVente.toLocalDateTime());
        }
        
        vente.setMontantTotal(rs.getDouble("montant_total"));
        
        // Configurer le client si présent
        int idClient = rs.getInt("id_client");
        if (idClient > 0 && !rs.wasNull()) {
            Client client = new Client();
            client.setIdClient(idClient);
            client.setNom(rs.getString("nom"));
            client.setPrenom(rs.getString("prenom"));
            client.setTelephone(rs.getString("telephone"));
            
            Date datePremierAchat = rs.getDate("date_premier_achat");
            if (datePremierAchat != null) {
                client.setDatePremierAchat(datePremierAchat.toLocalDate());
            }
            
            vente.setClient(client);
        }
        
        // Configurer l'utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUtilisateur(rs.getInt("id_utilisateur"));
        utilisateur.setNom(rs.getString("u.nom"));
        utilisateur.setPrenom(rs.getString("prenom"));
        utilisateur.setEmail(rs.getString("email"));
        utilisateur.setLogin(rs.getString("login"));
        vente.setUtilisateur(utilisateur);
        
        return vente;
    }
    
    private List<LigneVente> findLignesByVenteId(int idVente, Connection conn) throws SQLException {
        List<LigneVente> lignes = new ArrayList<>();
        String sql = "SELECT lv.*, m.* FROM ligne_vente lv " +
                    "JOIN medicament m ON lv.id_medicament = m.id_medicament " +
                    "WHERE lv.id_vente = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idVente);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                LigneVente ligne = new LigneVente();
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