/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

import java.util.ArrayList;
/**
 *
 * @author Sahid
 */
public class HospitalData {
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Appointment> appointments = new ArrayList<>();
    public static ArrayList<Hospitalization> hospitalizations = new ArrayList<>();

    static {
        users.add(new Administrator(
                0,
                "admin",
                "Administrador",
                "Sistema",
                "admin123"
        ));
    }

    public static Patient findPatientById(long id) {

        for (User user : users) {

            if (user instanceof Patient && user.getId() == id) {
                return (Patient) user;
            }
        }

        return null;
    }

    public static Doctor findDoctorById(long id) {

        for (User user : users) {

            if (user instanceof Doctor && user.getId() == id) {
                return (Doctor) user;
            }
        }

        return null;
    }

    public static User findUserByUsername(String username) {

        for (User user : users) {

            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    public static Appointment findAppointmentById(String id) {

        for (Appointment appointment : appointments) {

            if (appointment.getId().equals(id)) {
                return appointment;
            }
        }

        return null;
    }

    public static Hospitalization findHospitalizationById(String id) {

        for (Hospitalization hospitalization : hospitalizations) {

            if (hospitalization.getId().equals(id)) {
                return hospitalization;
            }
        }

        return null;
    }

    public static boolean usernameExists(String username) {

        return findUserByUsername(username) != null;
    }
}
