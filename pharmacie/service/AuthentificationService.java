package pharmacie.service;

import pharmacie.dao.UtilisateurDAO;
import pharmacie.model.entities.Utilisateur;
import pharmacie.model.enums.TypeUtilisateur;
import java.sql.SQLException;

public class AuthentificationService {
    private UtilisateurDAO utilisateurDAO;
    private static Utilisateur utilisateurConnecte;
    
    public AuthentificationService() {
        this.utilisateurDAO = new UtilisateurDAO();
    }
    
    public Utilisateur connecter(String login, String motDePasse) throws SQLException {
        Utilisateur utilisateur = utilisateurDAO.authenticate(login, motDePasse);
        if (utilisateur != null) {
            utilisateurConnecte = utilisateur;
        }
        return utilisateur;
    }
    
    public void deconnecter() {
        utilisateurConnecte = null;
    }
    
    public static Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
    
    public static boolean estConnecte() {
        return utilisateurConnecte != null;
    }
    
    public static boolean estAdministrateur() {
        return estConnecte() && utilisateurConnecte.getType() == TypeUtilisateur.ADMIN;
    }
    
    public void creerUtilisateur(Utilisateur utilisateur) throws SQLException {
        // Vérifier si le login existe déjà
        if (utilisateurDAO.loginExists(utilisateur.getLogin())) {
            throw new SQLException("Le login existe déjà");
        }
        
        utilisateurDAO.create(utilisateur);
    }
    
    public void changerMotDePasse(int idUtilisateur, String nouveauMotDePasse) throws SQLException {
        utilisateurDAO.updatePassword(idUtilisateur, nouveauMotDePasse);
    }
}
