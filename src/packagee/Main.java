/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

import com.formdev.flatlaf.FlatDarkLaf;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.UIManager;

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
        users.add(new Doctor(112234567890L, "doctor1", "Harry", "Kligman", "6070", Specialty.CARDIOLOGY, "L-1040010080 MTL", "O-201"));
        users.add(new Doctor(112234567890L, "doctor2", "Gabriela", "Torres", "0224", Specialty.PSYCHIATRY, "L-1040020080 MTL", "O-203"));
        users.add(new Doctor(112234567890L, "doctor3", "Orlando", "Arroyo", "9193", Specialty.NEUROLOGY, "L-1040030080 MTL", "O-205"));

        //Patients
        users.add(new Patient(152535455565L, "patient1", "Maria", "Yusti", "1506", "mariyusti@gmail.com", LocalDate.of(2005, 5, 15), true, 3101234567L, "Street 123"));
        users.add(new Patient(102030405060L, "patient2", "Juan", "Vizcaino", "0908", "juanvizcaino@gmail.com", LocalDate.of(2006, 5, 15), false, 3201234567L, "Street 456"));
        users.add(new Patient(204060806040L, "patient3", "Isac", "Monterrosa", "1224", "isacmonte@gmail.com", LocalDate.of(2004, 5, 15), false, 3301234567L, "Street 789"));
        
        //Create LoginController
        LoginController loginController = new LoginController(users, appointments, hospitalizations);

        //Launch LoginView
        java.awt.EventQueue.invokeLater(() ->
            new LoginView(loginController).setVisible(true)
        );
        
    }
}
