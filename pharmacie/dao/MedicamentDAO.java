package pharmacie.dao;

import pharmacie.model.entities.Medicament;
import pharmacie.model.exceptions.SeuilMinimumAtteintException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentDAO extends BaseDAO {
    
    // CRUD Operations
    public void create(Medicament medicament) throws SQLException {
        String sql = "INSERT INTO medicament (nom, dosage, forme, categorie, prix, quantite_stock, seuil_minimum) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, medicament.getNom());
            stmt.setString(2, medicament.getDosage());
            stmt.setString(3, medicament.getForme());
            stmt.setString(4, medicament.getCategorie());
            stmt.setDouble(5, medicament.getPrix());
            stmt.setInt(6, medicament.getQuantiteStock());
            stmt.setInt(7, medicament.getSeuilMinimum());
            
            stmt.executeUpdate();
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                medicament.setIdMedicament(rs.getInt(1));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
    }
    
    public Medicament findById(int id) throws SQLException {
        String sql = "SELECT * FROM medicament WHERE id_medicament = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Medicament medicament = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                medicament = mapResultSetToMedicament(rs);
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return medicament;
    }
    
    public List<Medicament> findAll() throws SQLException {
        String sql = "SELECT * FROM medicament ORDER BY nom";
        List<Medicament> medicaments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                medicaments.add(mapResultSetToMedicament(rs));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return medicaments;
    }
    
    public void update(Medicament medicament) throws SQLException {
        String sql = "UPDATE medicament SET nom = ?, dosage = ?, forme = ?, categorie = ?, " +
                     "prix = ?, quantite_stock = ?, seuil_minimum = ? WHERE id_medicament = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, medicament.getNom());
            stmt.setString(2, medicament.getDosage());
            stmt.setString(3, medicament.getForme());
            stmt.setString(4, medicament.getCategorie());
            stmt.setDouble(5, medicament.getPrix());
            stmt.setInt(6, medicament.getQuantiteStock());
            stmt.setInt(7, medicament.getSeuilMinimum());
            stmt.setInt(8, medicament.getIdMedicament());
            
            stmt.executeUpdate();
        } finally {
            closeResources(conn, stmt);
        }
    }
    
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM medicament WHERE id_medicament = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } finally {
            closeResources(conn, stmt);
        }
    }
    
    // Business Methods
    public void updateStock(int idMedicament, int quantite) throws SQLException {
        String sql = "UPDATE medicament SET quantite_stock = quantite_stock + ? WHERE id_medicament = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quantite);
            stmt.setInt(2, idMedicament);
            stmt.executeUpdate();
        } finally {
            closeResources(conn, stmt);
        }
    }
    
    public List<Medicament> findMedicamentsEnRupture() throws SQLException {
        String sql = "SELECT * FROM medicament WHERE quantite_stock <= 0 ORDER BY nom";
        return executeQueryForList(sql);
    }
    
    public List<Medicament> findMedicamentsSeuilAtteint() throws SQLException, SeuilMinimumAtteintException {
        String sql = "SELECT * FROM medicament WHERE quantite_stock <= seuil_minimum AND quantite_stock > 0 ORDER BY nom";
        List<Medicament> medicaments = executeQueryForList(sql);
        
        // Throw exception if any medication reaches threshold
        if (!medicaments.isEmpty()) {
            throw new SeuilMinimumAtteintException("Seuil minimum atteint pour certains médicaments");
        }
        
        return medicaments;
    }
    
    public List<Medicament> searchByNom(String searchTerm) throws SQLException {
        String sql = "SELECT * FROM medicament WHERE nom LIKE ? ORDER BY nom";
        List<Medicament> medicaments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchTerm + "%");
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                medicaments.add(mapResultSetToMedicament(rs));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return medicaments;
    }
    
    public double getValeurTotaleStock() throws SQLException {
        String sql = "SELECT SUM(prix * quantite_stock) as valeur_totale FROM medicament";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        double valeurTotale = 0;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                valeurTotale = rs.getDouble("valeur_totale");
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return valeurTotale;
    }
    
    // Helper Methods
    private Medicament mapResultSetToMedicament(ResultSet rs) throws SQLException {
        Medicament medicament = new Medicament();
        medicament.setIdMedicament(rs.getInt("id_medicament"));
        medicament.setNom(rs.getString("nom"));
        medicament.setDosage(rs.getString("dosage"));
        medicament.setForme(rs.getString("forme"));
        medicament.setCategorie(rs.getString("categorie"));
        medicament.setPrix(rs.getDouble("prix"));
        medicament.setQuantiteStock(rs.getInt("quantite_stock"));
        medicament.setSeuilMinimum(rs.getInt("seuil_minimum"));
        return medicament;
    }
    
    private List<Medicament> executeQueryForList(String sql) throws SQLException {
        List<Medicament> medicaments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                medicaments.add(mapResultSetToMedicament(rs));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return medicaments;
    }
}
