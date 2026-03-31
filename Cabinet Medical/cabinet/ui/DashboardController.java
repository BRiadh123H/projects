package cabinet.ui;

import cabinet.dao.AppointmentDAO;
import cabinet.dao.PatientDAO;
import cabinet.model.Appointment;
import cabinet.model.Patient;
import cabinet.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label patientCountLabel;
    @FXML private Label todayApptLabel;
    @FXML private Label revenueLabel;
    @FXML private Label debtLabel;
    
    @FXML private TextField searchField;
    
    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, String> colFirstName;
    @FXML private TableColumn<Patient, String> colLastName;
    @FXML private TableColumn<Patient, String> colPhone;
    @FXML private TableColumn<Patient, Integer> colPaid;
    @FXML private TableColumn<Patient, Integer> colRemaining;
    
    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, String> colDate;
    @FXML private TableColumn<Appointment, String> colReason;
    
    @FXML private Button logoutButton;

    private PatientDAO patientDAO = new PatientDAO();
    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private ObservableList<Patient> masterPatientData = FXCollections.observableArrayList();
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        if (welcomeLabel != null && user != null) {
            welcomeLabel.setText("Welcome, " + (user.getRole().equals("Doctor") ? "Dr. " : "") + user.getUsername());
        }
    }

    @FXML
    public void initialize() {
        // Setup Patient Table columns
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colPaid.setCellValueFactory(new PropertyValueFactory<>("whatsPaid"));
        colRemaining.setCellValueFactory(new PropertyValueFactory<>("remainingMoney"));

        // Setup Appointment Table columns
        colDate.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));

        // Load data and calculate stats
        refreshData();

        // Search logic (Filters patient name or phone)
        FilteredList<Patient> filteredData = new FilteredList<>(masterPatientData, p -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(patient -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return patient.getFirstName().toLowerCase().contains(lower) || 
                       patient.getLastName().toLowerCase().contains(lower) ||
                       (patient.getPhone() != null && patient.getPhone().contains(lower));
            });
        });
        patientTable.setItems(filteredData);

        // Row selection logic: Show appointments for selected patient
        patientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showAppointments(newVal.getId());
            }
        });

        // Logout logic
        logoutButton.setOnAction(e -> {
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.close();
            new LoginFx().start(new Stage());
        });
    }

    private void refreshData() {
        List<Patient> patients = patientDAO.getAllPatients();
        masterPatientData.setAll(patients);
        
        long totalRevenue = 0;
        long totalDebt = 0;
        int todayAppts = 0;
        LocalDate today = LocalDate.now();

        for (Patient p : patients) {
            totalRevenue += p.getWhatsPaid();
            totalDebt += p.getRemainingMoney();
            
            // Count today's appointments across all patients
            List<Appointment> appts = appointmentDAO.getAppointmentsByPatient(p.getId());
            for (Appointment a : appts) {
                if (a.getAppointmentDate().toLocalDate().equals(today)) {
                    todayAppts++;
                }
            }
        }

        // Update top cards
        patientCountLabel.setText(String.valueOf(patients.size()));
        todayApptLabel.setText(String.valueOf(todayAppts));
        revenueLabel.setText(String.format("%,d", totalRevenue));
        debtLabel.setText(String.format("%,d", totalDebt));
    }

    private void showAppointments(int patientId) {
        List<Appointment> appts = appointmentDAO.getAppointmentsByPatient(patientId);
        appointmentTable.setItems(FXCollections.observableArrayList(appts));
    }
}
