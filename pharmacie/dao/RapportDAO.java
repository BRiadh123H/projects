package pharmacie.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RapportDAO extends BaseDAO {
    
    // Statistiques de vente
    public Map<String, Object> getStatistiquesVentes(LocalDate dateDebut, LocalDate dateFin) throws SQLException {
        Map<String, Object> statistiques = new HashMap<>();
        String sql = "SELECT " +
                    "COUNT(*) as nombre_ventes, " +
                    "COALESCE(SUM(montant_total), 0) as chiffre_affaires, " +
                    "COALESCE(AVG(montant_total), 0) as panier_moyen, " +
                    "COALESCE(SUM(lv.quantite), 0) as articles_vendus " +
                    "FROM vente v " +
                    "LEFT JOIN ligne_vente lv ON v.id_vente = lv.id_vente " +
                    "WHERE DATE(v.date_vente) BETWEEN ? AND ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(dateDebut));
            stmt.setDate(2, Date.valueOf(dateFin));
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                statistiques.put("nombreVentes", rs.getInt("nombre_ventes"));
                statistiques.put("chiffreAffaires", rs.getDouble("chiffre_affaires"));
                statistiques.put("panierMoyen", rs.getDouble("panier_moyen"));
                statistiques.put("articlesVendus", rs.getInt("articles_vendus"));
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return statistiques;
    }
    
    // Top des médicaments vendus
    public List<Map<String, Object>> getTopMedicamentsVendus(int limit, LocalDate dateDebut, LocalDate dateFin) throws SQLException {
        List<Map<String, Object>> topMedicaments = new ArrayList<>();
        String sql = "SELECT m.nom, SUM(lv.quantite) as quantite_vendue, " +
                    "SUM(lv.quantite * lv.prix_unitaire) as chiffre_affaires " +
                    "FROM ligne_vente lv " +
                    "JOIN medicament m ON lv.id_medicament = m.id_medicament " +
                    "JOIN vente v ON lv.id_vente = v.id_vente " +
                    "WHERE DATE(v.date_vente) BETWEEN ? AND ? " +
                    "GROUP BY m.id_medicament, m.nom " +
                    "ORDER BY quantite_vendue DESC " +
                    "LIMIT ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(dateDebut));
            stmt.setDate(2, Date.valueOf(dateFin));
            stmt.setInt(3, limit);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> medicament = new HashMap<>();
                medicament.put("nom", rs.getString("nom"));
                medicament.put("quantiteVendue", rs.getInt("quantite_vendue"));
                medicament.put("chiffreAffaires", rs.getDouble("chiffre_affaires"));
                topMedicaments.add(medicament);
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return topMedicaments;
    }
    
    // Performance des fournisseurs
    public List<Map<String, Object>> getPerformanceFournisseurs(LocalDate dateDebut, LocalDate dateFin) throws SQLException {
        List<Map<String, Object>> performanceFournisseurs = new ArrayList<>();
        String sql = "SELECT f.nom, COUNT(c.id_commande) as nombre_commandes, " +
                    "COALESCE(SUM(lc.quantite * lc.prix_unitaire), 0) as montant_total, " +
                    "AVG(DATEDIFF(c.date_reception, c.date_commande)) as delai_moyen " +
                    "FROM fournisseur f " +
                    "LEFT JOIN commande_fournisseur c ON f.id_fournisseur = c.id_fournisseur " +
                    "LEFT JOIN ligne_commande lc ON c.id_commande = lc.id_commande " +
                    "WHERE c.statut = 'LIVRE' AND c.date_commande BETWEEN ? AND ? " +
                    "GROUP BY f.id_fournisseur, f.nom " +
                    "ORDER BY montant_total DESC";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(dateDebut));
            stmt.setDate(2, Date.valueOf(dateFin));
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> fournisseur = new HashMap<>();
                fournisseur.put("nom", rs.getString("nom"));
                fournisseur.put("nombreCommandes", rs.getInt("nombre_commandes"));
                fournisseur.put("montantTotal", rs.getDouble("montant_total"));
                
                double delaiMoyen = rs.getDouble("delai_moyen");
                fournisseur.put("delaiMoyen", rs.wasNull() ? 0 : delaiMoyen);
                
                performanceFournisseurs.add(fournisseur);
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return performanceFournisseurs;
    }
    
    // Évolution du chiffre d'affaires mensuel
    public List<Map<String, Object>> getEvolutionCAMensuel(int annee) throws SQLException {
        List<Map<String, Object>> evolution = new ArrayList<>();
        String sql = "SELECT MONTH(date_vente) as mois, " +
                    "COALESCE(SUM(montant_total), 0) as chiffre_affaires, " +
                    "COUNT(*) as nombre_ventes " +
                    "FROM vente " +
                    "WHERE YEAR(date_vente) = ? " +
                    "GROUP BY MONTH(date_vente) " +
                    "ORDER BY mois";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, annee);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> mois = new HashMap<>();
                mois.put("mois", rs.getInt("mois"));
                mois.put("chiffreAffaires", rs.getDouble("chiffre_affaires"));
                mois.put("nombreVentes", rs.getInt("nombre_ventes"));
                evolution.add(mois);
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return evolution;
    }
    
    // Clients les plus fidèles
    public List<Map<String, Object>> getClientsFideles(int limit) throws SQLException {
        List<Map<String, Object>> clientsFideles = new ArrayList<>();
        String sql = "SELECT c.nom, c.prenom, c.telephone, " +
                    "COUNT(v.id_vente) as nombre_achats, " +
                    "COALESCE(SUM(v.montant_total), 0) as montant_total, " +
                    "MAX(v.date_vente) as dernier_achat " +
                    "FROM client c " +
                    "LEFT JOIN vente v ON c.id_client = v.id_client " +
                    "GROUP BY c.id_client, c.nom, c.prenom, c.telephone " +
                    "HAVING nombre_achats > 0 " +
                    "ORDER BY montant_total DESC " +
                    "LIMIT ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, limit);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> client = new HashMap<>();
                client.put("nom", rs.getString("nom"));
                client.put("prenom", rs.getString("prenom"));
                client.put("telephone", rs.getString("telephone"));
                client.put("nombreAchats", rs.getInt("nombre_achats"));
                client.put("montantTotal", rs.getDouble("montant_total"));
                
                Timestamp dernierAchat = rs.getTimestamp("dernier_achat");
                if (dernierAchat != null) {
                    client.put("dernierAchat", dernierAchat.toLocalDateTime());
                }
                
                clientsFideles.add(client);
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return clientsFideles;
    }
    
    // Alertes de stock (médicaments en rupture ou proches du seuil)
    public List<Map<String, Object>> getAlertesStock() throws SQLException {
        List<Map<String, Object>> alertes = new ArrayList<>();
        String sql = "SELECT nom, quantite_stock, seuil_minimum, " +
                    "CASE " +
                    "  WHEN quantite_stock <= 0 THEN 'RUPTURE' " +
                    "  WHEN quantite_stock <= seuil_minimum THEN 'SEUIL_ATTEINT' " +
                    "  ELSE 'NORMAL' " +
                    "END as niveau_alerte " +
                    "FROM medicament " +
                    "WHERE quantite_stock <= seuil_minimum " +
                    "ORDER BY niveau_alerte, quantite_stock";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> alerte = new HashMap<>();
                alerte.put("nom", rs.getString("nom"));
                alerte.put("quantiteStock", rs.getInt("quantite_stock"));
                alerte.put("seuilMinimum", rs.getInt("seuil_minimum"));
                alerte.put("niveauAlerte", rs.getString("niveau_alerte"));
                alertes.add(alerte);
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return alertes;
    }
}
