package pharmacie.dao;

import pharmacie.model.entities.Fournisseur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FournisseurDAO extends BaseDAO {
    
    public void create(Fournisseur fournisseur) throws SQLException {
        String sql = "INSERT INTO fournisseur (nom, adresse, telephone, email) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, fournisseur.getNom());
            stmt.setString(2, fournisseur.getAdresse());
            stmt.setString(3, fournisseur.getTelephone());
            stmt.setString(4, fournisseur.getEmail());
            
            stmt.executeUpdate();
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                fournisseur.setIdFournisseur(rs.getInt(1));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
    }
    
    public Fournisseur findById(int id) throws SQLException {
        String sql = "SELECT * FROM fournisseur WHERE id_fournisseur = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Fournisseur fournisseur = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                fournisseur = mapResultSetToFournisseur(rs);
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return fournisseur;
    }
    
    public List<Fournisseur> findAll() throws SQLException {
        String sql = "SELECT * FROM fournisseur ORDER BY nom";
        List<Fournisseur> fournisseurs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                fournisseurs.add(mapResultSetToFournisseur(rs));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return fournisseurs;
    }
    
    public void update(Fournisseur fournisseur) throws SQLException {
        String sql = "UPDATE fournisseur SET nom = ?, adresse = ?, telephone = ?, email = ? " +
                     "WHERE id_fournisseur = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, fournisseur.getNom());
            stmt.setString(2, fournisseur.getAdresse());
            stmt.setString(3, fournisseur.getTelephone());
            stmt.setString(4, fournisseur.getEmail());
            stmt.setInt(5, fournisseur.getIdFournisseur());
            
            stmt.executeUpdate();
        } finally {
            closeResources(conn, stmt);
        }
    }
    
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM fournisseur WHERE id_fournisseur = ?";
        
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
    
    public List<Fournisseur> searchByNom(String searchTerm) throws SQLException {
        String sql = "SELECT * FROM fournisseur WHERE nom LIKE ? ORDER BY nom";
        List<Fournisseur> fournisseurs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchTerm + "%");
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                fournisseurs.add(mapResultSetToFournisseur(rs));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return fournisseurs;
    }
    
    private Fournisseur mapResultSetToFournisseur(ResultSet rs) throws SQLException {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setIdFournisseur(rs.getInt("id_fournisseur"));
        fournisseur.setNom(rs.getString("nom"));
        fournisseur.setAdresse(rs.getString("adresse"));
        fournisseur.setTelephone(rs.getString("telephone"));
        fournisseur.setEmail(rs.getString("email"));
        return fournisseur;
    }
}
