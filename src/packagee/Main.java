/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

import com.formdev.flatlaf.FlatDarkLaf;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.UIManager;
import modelo.Administrator;
import modelo.Appointment;
import modelo.AppointmentStatus;
import modelo.Doctor;
import modelo.Hospitalization;
import modelo.Patient;
import modelo.Specialty;
import modelo.User;

/**
 *
 * @author AxelitoThunder
 */
public class Main {
    public static void main(String[] args) {

        //FlatLaf
        System.setProperty("flatlaf.useNativeLibrary", "false");
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        ArrayList<User> users = new ArrayList<>();
        ArrayList<Appointment> appointments = new ArrayList<>();
        ArrayList<Hospitalization> hospitalizations = new ArrayList<>();

        //Admins
        users.add(new Administrator(0, "maricami", "admin1", "admin11", "maricami"));
        users.add(new Administrator(0, "axel", "admin2", "admin22", "axel"));
        users.add(new Administrator(0, "sahid", "admin3", "admin33", "sahid"));
        users.add(new Administrator(0, "eduardo", "admin4", "admin44", "eduardo"));
        users.add(new Administrator(0, "jefferson", "admin5", "admin55", "jefferson"));
        
        //Doctors
        Doctor doctor1 = new Doctor(112234567890L, "doctor1", "Harry", "Kligman", "6070", Specialty.CARDIOLOGY, "L-1040010080 MTL", "O-201");
        Doctor doctor2 = new Doctor(122334455667L, "doctor2", "Gabriela", "Torres", "0224", Specialty.PSYCHIATRY, "L-1040020080 MTL", "O-203");
        Doctor doctor3 = new Doctor(133445566778L, "doctor3", "Orlando","Arroyo", "9193", Specialty.NEUROLOGY, "L-1040030080 MTL", "O-205");

        users.add(doctor1);
        users.add(doctor2);
        users.add(doctor3);

        //Patients
        Patient patient1 = new Patient(152535455565L, "patient1", "Maria", "Yusti","1506", "mariyusti@gmail.com", LocalDate.of(2005, 5, 15), true, 3101234567L, "Street 123");
        Patient patient2 = new Patient(102030405060L, "patient2", "Juan", "Vizcaino", "0908", "juanvizcaino@gmail.com", LocalDate.of(2006, 5, 15), false, 3201234567L, "Street 456");
        Patient patient3 = new Patient(204060806040L, "patient3", "Isac", "Monterrosa", "1224", "isacmonte@gmail.com",LocalDate.of(2004, 5, 15), false, 3301234567L, "Street 789");

        users.add(patient1);
        users.add(patient2);
        users.add(patient3);

        //Appointments
        Appointment ap1 = new Appointment("A-152535455565-0000", patient1, doctor1, Specialty.CARDIOLOGY, LocalDateTime.of(2026, 5, 25, 9, 0), "Dolor en el pecho", true);
        Appointment ap2 = new Appointment("A-152535455565-0001", patient1, doctor3, Specialty.NEUROLOGY, LocalDateTime.of(2026, 5, 26, 10, 15), "Migrañas constantes", false);
        Appointment ap3 = new Appointment("A-102030405060-0000", patient2, doctor2, Specialty.PSYCHIATRY, LocalDateTime.of(2026, 5, 27, 11, 30), "Ansiedad y estrés", true);
        Appointment ap4 = new Appointment("A-204060806040-0000", patient3, doctor1, Specialty.CARDIOLOGY, LocalDateTime.of(2026, 5, 28, 14, 45), "Palpitaciones frecuentes", false);

        //Status
        ap1.setStatus(AppointmentStatus.PENDING);
        ap2.setStatus(AppointmentStatus.COMPLETED);
        ap3.setStatus(AppointmentStatus.REQUESTED);
        ap4.setStatus(AppointmentStatus.CANCELED);

        //Medical information
        ap2.setDiagnosis("Migraña crónica");
        ap2.setObservations("Sensibilidad a la luz");
        ap2.setRecommendedTreatment("Analgésicos y descanso");
        ap2.setFollowUp("Control en 30 días");

        appointments.add(ap1);
        appointments.add(ap2);
        appointments.add(ap3);
        appointments.add(ap4);

        
        //Create LoginController
        LoginController loginController = new LoginController(users, appointments, hospitalizations);

        //Launch LoginView
        java.awt.EventQueue.invokeLater(() ->
            new LoginView(loginController).setVisible(true)
        );
        
    }
}
