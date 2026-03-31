package cabinet.dao;

import cabinet.DatabaseConnection;
import cabinet.model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    // Method to schedule a new appointment
    public void addAppointment(Appointment appointment) {
        String query = "INSERT INTO appointments (patient_id, appointment_date, reason) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, appointment.getPatientId());
            // Converting Java LocalDateTime to SQL Timestamp
            pstmt.setTimestamp(2, Timestamp.valueOf(appointment.getAppointmentDate()));
            pstmt.setString(3, appointment.getReason());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get all appointments for a specific patient
    public List<Appointment> getAppointmentsByPatient(int patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM appointments WHERE patient_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, patientId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(new Appointment(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getTimestamp("appointment_date").toLocalDateTime(), // Converting SQL Timestamp back to Java LocalDateTime
                        rs.getString("reason")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
}
