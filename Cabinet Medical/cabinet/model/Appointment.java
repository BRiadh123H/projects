package cabinet.model;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private int patientId;
    private LocalDateTime appointmentDate;
    private String reason;

    public Appointment(int id, int patientId, LocalDateTime appointmentDate, String reason) {
        this.id = id;
        this.patientId = patientId;
        this.appointmentDate = appointmentDate;
        this.reason = reason;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
