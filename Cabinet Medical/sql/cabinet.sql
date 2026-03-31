CREATE DATABASE IF NOT EXISTS cabinet_medical;
USE cabinet_medical;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('Doctor', 'Secretary') NOT NULL
);

CREATE TABLE IF NOT EXISTS patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    remaining_money INT DEFAULT 0,
    whats_paid INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    appointment_date DATETIME NOT NULL,
    reason TEXT,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

-- Default users
INSERT IGNORE INTO users (username, password, role) VALUES ('doctor', 'doc123', 'Doctor');
INSERT IGNORE INTO users (username, password, role) VALUES ('secretary', 'sec123', 'Secretary');

-- Sample Patients
INSERT IGNORE INTO patients (first_name, last_name, phone, email, remaining_money, whats_paid) VALUES 
('Ahmed', 'Benali', '0550123456', 'ahmed.b@email.com', 500, 1500),
('Sara', 'Mansouri', '0661987654', 'sara.m@email.com', 0, 2000),
('Mohamed', 'Kacem', '0772345678', 'm.kacem@email.com', 1200, 800),
('Fatima', 'Zahra', '0555112233', 'fatima.z@email.com', 300, 1700),
('Yassine', 'Toumi', '0666445566', 'yassine.t@email.com', 0, 2500),
('Lina', 'Haddad', '0555778899', 'lina.h@email.com', 1000, 4000),
('Omar', 'Farah', '0660334455', 'omar.f@email.com', 0, 1500),
('Zineb', 'Amrani', '0778889900', 'zineb.a@email.com', 200, 1800),
('Karim', 'Sadek', '0552112233', 'karim.s@email.com', 1500, 500),
('Sofia', 'Berada', '0663445566', 'sofia.b@email.com', 0, 3000),
('Walid', 'Moussaoui', '0771223344', 'walid.m@email.com', 800, 1200),
('Amel', 'Rezgui', '0559887766', 'amel.r@email.com', 400, 1600),
('Hamza', 'Saidani', '0667112233', 'hamza.s@email.com', 0, 1000),
('Nadia', 'Bouchard', '0774332211', 'nadia.b@email.com', 2500, 0),
('Rami', 'Guerroudj', '0554556677', 'rami.g@email.com', 0, 2200);

-- Sample Appointments
INSERT IGNORE INTO appointments (patient_id, appointment_date, reason) VALUES 
(1, '2026-03-14 09:30:00', 'Routine check-up'),
(1, '2026-03-21 10:00:00', 'Review of previous blood tests'),
(2, '2026-03-14 11:00:00', 'Consultation for recurring knee pain'),
(3, '2026-03-15 14:00:00', 'Blood test results and prescription adjustment'),
(4, '2026-03-16 08:30:00', 'General examination and height/weight measurement'),
(5, '2026-03-16 12:00:00', 'Seasonal flu vaccination'),
(6, '2026-03-17 10:30:00', 'Dental cleaning follow-up (referred)'),
(7, '2026-03-18 15:00:00', 'Medication renewal for diabetes'),
(8, '2026-03-19 09:00:00', 'Eye examination follow-up'),
(9, '2026-03-19 14:30:00', 'Emergency consultation: persistent headache'),
(10, '2026-03-20 11:00:00', 'Physical therapy session'),
(11, '2026-03-21 13:00:00', 'Post-operative check-up'),
(12, '2026-03-22 08:45:00', 'Nutritional guidance session'),
(13, '2026-03-23 16:00:00', 'Psychological consultation'),
(14, '2026-03-24 10:00:00', 'New patient intake interview'),
(15, '2026-03-25 14:00:00', 'Travel vaccination: Yellow Fever'),
(1, '2026-04-05 10:00:00', 'Monthly progress review');
