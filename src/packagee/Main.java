/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

import com.formdev.flatlaf.FlatDarkLaf;
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
        
        //Create controller
        LoginController loginController = new LoginController(users, appointments, hospitalizations);

        //Launch LoginView
        java.awt.EventQueue.invokeLater(() ->
            new LoginView(loginController).setVisible(true)
        );
    }
}
