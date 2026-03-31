package pharmacie.vue;

import pharmacie.service.AuthentificationService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField txtLogin;
    private JPasswordField txtMotDePasse;
    private JButton btnConnecter;
    private JButton btnQuitter;
    private AuthentificationService authService;
    
    public LoginFrame() {
        authService = new AuthentificationService();
        initComponents();
        setTitle("Connexion - Système de Gestion de Pharmacie");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void initComponents() {
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel du titre
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Connexion au Système");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        
        // Panel des champs de saisie
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Login
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Login:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        txtLogin = new JTextField(15);
        formPanel.add(txtLogin, gbc);
        
        // Mot de passe
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Mot de passe:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        txtMotDePasse = new JPasswordField(15);
        formPanel.add(txtMotDePasse, gbc);
        
        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnConnecter = new JButton("Se connecter");
        btnQuitter = new JButton("Quitter");
        buttonPanel.add(btnConnecter);
        buttonPanel.add(btnQuitter);
        
        // Ajout des panels au panel principal
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Ajout du panel principal à la fenêtre
        add(mainPanel);
        
        // Gestion des événements
        btnConnecter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connecter();
            }
        });
        
        btnQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        // Permettre la connexion avec la touche Entrée
        txtMotDePasse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connecter();
            }
        });
    }
    
    private void connecter() {
        String login = txtLogin.getText().trim();
        String motDePasse = new String(txtMotDePasse.getPassword());
        
        if (login.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez saisir le login et le mot de passe", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            if (authService.connecter(login, motDePasse) != null) {
                JOptionPane.showMessageDialog(this, 
                    "Connexion réussie !\nBienvenue " + login, 
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Ouvrir la fenêtre principale
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        MainFrame mainFrame = new MainFrame();
                        mainFrame.setVisible(true);
                        dispose(); // Fermer la fenêtre de connexion
                    }
                });
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Login ou mot de passe incorrect", 
                    "Échec de connexion", 
                    JOptionPane.ERROR_MESSAGE);
                txtMotDePasse.setText("");
                txtMotDePasse.requestFocus();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erreur de connexion à la base de données: " + ex.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }
}