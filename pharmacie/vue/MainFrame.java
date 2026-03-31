package pharmacie.vue;

import pharmacie.service.AuthentificationService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private GestionStockPanel stockPanel;
    private GestionVentesPanel ventesPanel;
    private GestionCommandesPanel commandesPanel;
    private GestionClientsPanel clientsPanel;
    private RapportsPanel rapportsPanel;
    
    public MainFrame() {
        initComponents();
        setTitle("Système de Gestion de Pharmacie - " + 
                 AuthentificationService.getUtilisateurConnecte().getNomComplet());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        // Demander confirmation avant de fermer
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmerFermeture();
            }
        });
    }
    
    private void initComponents() {
        // Créer la barre de menu
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Fichier
        JMenu menuFichier = new JMenu("Fichier");
        JMenuItem menuItemDeconnexion = new JMenuItem("Déconnexion");
        JMenuItem menuItemQuitter = new JMenuItem("Quitter");
        
        menuItemDeconnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deconnecter();
            }
        });
        
        menuItemQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmerFermeture();
            }
        });
        
        menuFichier.add(menuItemDeconnexion);
        menuFichier.addSeparator();
        menuFichier.add(menuItemQuitter);
        
        // Menu Aide
        JMenu menuAide = new JMenu("Aide");
        JMenuItem menuItemAPropos = new JMenuItem("À propos");
        
        menuItemAPropos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherAPropos();
            }
        });
        
        menuAide.add(menuItemAPropos);
        
        menuBar.add(menuFichier);
        menuBar.add(menuAide);
        setJMenuBar(menuBar);
        
        // Créer le panneau à onglets
        tabbedPane = new JTabbedPane();
        
        // Ajouter les différents panels
        stockPanel = new GestionStockPanel();
        ventesPanel = new GestionVentesPanel();
        commandesPanel = new GestionCommandesPanel();
        clientsPanel = new GestionClientsPanel();
        rapportsPanel = new RapportsPanel();
        
        tabbedPane.addTab("📦 Gestion Stock", stockPanel);
        tabbedPane.addTab("💰 Ventes", ventesPanel);
        tabbedPane.addTab("📋 Commandes", commandesPanel);
        tabbedPane.addTab("👥 Clients", clientsPanel);
        tabbedPane.addTab("📊 Rapports", rapportsPanel);
        
        // Panel d'information utilisateur
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel userLabel = new JLabel("Connecté en tant que: " + 
                                     AuthentificationService.getUtilisateurConnecte().getNomComplet() + 
                                     " (" + AuthentificationService.getUtilisateurConnecte().getType() + ")");
        userPanel.add(userLabel);
        
        // Configuration du layout principal
        setLayout(new BorderLayout());
        add(userPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        
        // Afficher un message de bienvenue
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(MainFrame.this,
                    "Bienvenue dans le système de gestion de pharmacie !\n" +
                    "Vous êtes connecté en tant que " + 
                    AuthentificationService.getUtilisateurConnecte().getType().getLibelle(),
                    "Bienvenue",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    
    private void deconnecter() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir vous déconnecter ?",
            "Confirmation de déconnexion",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            AuthentificationService.getUtilisateurConnecte();
            new AuthentificationService().deconnecter();
            
            // Retourner à la fenêtre de connexion
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                    dispose();
                }
            });
        }
    }
    
    private void confirmerFermeture() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir quitter l'application ?",
            "Confirmation de fermeture",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    private void afficherAPropos() {
        JOptionPane.showMessageDialog(this,
            "Système de Gestion de Pharmacie\n" +
            "Version 1.0\n\n" +
            "Développé par:\n" +
            "• [Nom du groupe]\n" +
            "• [Membres]\n\n" +
            "© 2026 - Tous droits réservés",
            "À propos",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void rafraichirTousLesPanels() {
        stockPanel.rafraichir();
        ventesPanel.rafraichir();
        commandesPanel.rafraichir();
        clientsPanel.rafraichir();
        rapportsPanel.rafraichir();
    }
}