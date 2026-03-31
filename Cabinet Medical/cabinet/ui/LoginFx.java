package cabinet.ui;

import cabinet.dao.UserDAO;
import cabinet.model.User;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginFx extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cabinet Medical - Login");

        // Main container
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setStyle("-fx-background-color: #f8f9fa;");

        // ===== TITLE =====
        Label titleLabel = new Label("Cabinet Medical");
        titleLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        titleLabel.setPadding(new Insets(0, 0, 20, 0));

        // ===== FORM PANEL =====
        GridPane formPanel = new GridPane();
        formPanel.setHgap(15);
        formPanel.setVgap(15);
        formPanel.setAlignment(Pos.CENTER);

        // Username
        Label lblUser = new Label("Username:");
        lblUser.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setStyle("-fx-font-size: 14px; -fx-pref-width: 250px; -fx-pref-height: 35px;");

        // Password
        Label lblPass = new Label("Password:");
        lblPass.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setStyle("-fx-font-size: 14px; -fx-pref-width: 250px; -fx-pref-height: 35px;");

        formPanel.add(lblUser, 0, 0);
        formPanel.add(usernameField, 1, 0);
        formPanel.add(lblPass, 0, 1);
        formPanel.add(passwordField, 1, 1);

        // ===== BUTTON =====
        Button loginButton = new Button("Login");
        String btnStyle = "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; " +
                         "-fx-background-color: #3498db; -fx-pref-width: 150px; -fx-pref-height: 40px; " +
                         "-fx-background-radius: 5px; -fx-cursor: hand;";
        loginButton.setStyle(btnStyle);

        loginButton.setOnMouseEntered(e -> loginButton.setStyle(btnStyle + "-fx-background-color: #2980b9;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(btnStyle));

        Label statusLabel = new Label();

        UserDAO userDAO = new UserDAO();

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            User user = userDAO.validateLogin(username, password);

            if (user != null) {
                try {
                    primaryStage.close();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                    Scene dashboardScene = new Scene(loader.load());
                    dashboardScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                    
                    DashboardController controller = loader.getController();
                    controller.setUser(user);
                    
                    Stage dashboardStage = new Stage();
                    dashboardStage.setTitle("Cabinet Medical - Dashboard");
                    dashboardStage.setScene(dashboardScene);
                    dashboardStage.setMaximized(true);
                    dashboardStage.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    statusLabel.setText("Error loading dashboard: " + ex.getMessage());
                }
            } else {
                statusLabel.setText("Invalid username or password");
                statusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            }
        });

        mainContainer.getChildren().addAll(titleLabel, formPanel, loginButton, statusLabel);

        Scene scene = new Scene(mainContainer, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
