package cabinet.dao;

import cabinet.DatabaseConnection;
import cabinet.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // Method to add a new patient to the database
    public void addPatient(Patient patient) {
        String query = "INSERT INTO patients (first_name, last_name, phone, email, remaining_money, whats_paid) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, patient.getFirstName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setString(3, patient.getPhone());
            pstmt.setString(4, patient.getEmail());
            pstmt.setInt(5, patient.getRemainingMoney());
            pstmt.setInt(6, patient.getWhatsPaid());
            
            pstmt.executeUpdate(); // executeUpdate is used for INSERT, UPDATE, DELETE
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get all patients from the database
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patients";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                patients.add(new Patient(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getInt("remaining_money"),
                    rs.getInt("whats_paid")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }
}
