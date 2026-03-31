package pharmacie.dao;

import pharmacie.model.entities.Utilisateur;
import pharmacie.model.enums.TypeUtilisateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO extends BaseDAO {
    
    public Utilisateur findByLogin(String login) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE login = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Utilisateur utilisateur = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                utilisateur = mapResultSetToUtilisateur(rs);
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return utilisateur;
    }
    
    public Utilisateur authenticate(String login, String password) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE login = ? AND mot_de_passe = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Utilisateur utilisateur = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                utilisateur = mapResultSetToUtilisateur(rs);
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return utilisateur;
    }
    
    public void create(Utilisateur utilisateur) throws SQLException {
        String sql = "INSERT INTO utilisateur (nom, prenom, email, login, mot_de_passe, type) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getLogin());
            stmt.setString(5, utilisateur.getMotDePasse());
            stmt.setString(6, utilisateur.getType().name());
            
            stmt.executeUpdate();
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                utilisateur.setIdUtilisateur(rs.getInt(1));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
    }
    
    public List<Utilisateur> findAll() throws SQLException {
        String sql = "SELECT * FROM utilisateur ORDER BY nom, prenom";
        List<Utilisateur> utilisateurs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                utilisateurs.add(mapResultSetToUtilisateur(rs));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return utilisateurs;
    }
    
    public void updatePassword(int idUtilisateur, String newPassword) throws SQLException {
        String sql = "UPDATE utilisateur SET mot_de_passe = ? WHERE id_utilisateur = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, newPassword);
            stmt.setInt(2, idUtilisateur);
            stmt.executeUpdate();
        } finally {
            closeResources(conn, stmt);
        }
    }
    
    public boolean loginExists(String login) throws SQLException {
        String sql = "SELECT COUNT(*) FROM utilisateur WHERE login = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean exists = false;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return exists;
    }
    
    private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUtilisateur(rs.getInt("id_utilisateur"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setPrenom(rs.getString("prenom"));
        utilisateur.setEmail(rs.getString("email"));
        utilisateur.setLogin(rs.getString("login"));
        utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
        utilisateur.setType(TypeUtilisateur.valueOf(rs.getString("type")));
        return utilisateur;
    }
}