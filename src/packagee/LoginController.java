/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

import java.time.LocalDate;
import java.util.ArrayList;
import modelo.Administrator;
import modelo.Appointment;
import modelo.Doctor;
import modelo.Hospitalization;
import modelo.Patient;
import modelo.User;

/**
 *
 * @author Sahid
 */
public class LoginController {

    private ArrayList<User> users;
    private ArrayList<Appointment> appointments;
    private ArrayList<Hospitalization> hospitalizations;
    
    public LoginController(ArrayList<User> users, ArrayList<Appointment> appointments, ArrayList<Hospitalization> hospitalizations) {
        this.users = users;
        this.appointments = appointments;
        this.hospitalizations = hospitalizations;
    }
    
    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public ArrayList<Hospitalization> getHospitalizations() {
        return hospitalizations;
    }
    
    public Response login(String username, String password) {

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return new Response(StatusCode.BAD_REQUEST, "Complete all fields");
        }

        for (User user : users) {
            if (username.equals(user.getUsername())) {
                if (!password.equals(user.getPassword())) {
                    return new Response(StatusCode.BAD_REQUEST, "Incorrect password");
                }

                String role;

                if (user instanceof Administrator) {
                    role = "ADMIN";
                } else if (user instanceof Doctor) {
                    role = "DOCTOR";
                } else {
                    role = "PATIENT";
                }

                return new Response(StatusCode.OK, "Login successful", role + ";" + user.getUsername());
            }
        }

        return new Response(StatusCode.NOT_FOUND, "User not found");
    }
    
    public Response registerPatient(String idText, String username, String firstname, String lastname, String password, String confirmPassword, String email, String birthdateText, String genderText, String phoneText, String address) {
        if (firstname.trim().isEmpty() || lastname.trim().isEmpty()
                || username.trim().isEmpty() || password.trim().isEmpty()) {
            return new Response(StatusCode.BAD_REQUEST, "Complete all fields");
        }

        if (!password.equals(confirmPassword)) {
            return new Response(StatusCode.BAD_REQUEST, "Passwords do not match");
        }
        long id;
        long phone;

        try {
            id = Long.parseLong(idText);
            phone = Long.parseLong(phoneText);
        } catch (NumberFormatException e) {
            return new Response(StatusCode.BAD_REQUEST, "ID or phone must be numeric");
        }

        boolean gender;
        if (genderText.equals("Female")) {
            gender = false;
        } else if (genderText.equals("Male")) {
            gender = true;
        } else {
            return new Response(StatusCode.BAD_REQUEST, "Select gender");
        }

        LocalDate birthdate;
        try {
            birthdate = LocalDate.parse(birthdateText);
        } catch (Exception e) {
            return new Response(StatusCode.BAD_REQUEST, "Invalid birthdate format (YYYY-MM-DD)");
        }

        users.add(new Patient(id, username, firstname, lastname, password,
                email, birthdate, gender, phone, address));
        
        HospitalData.fireTableChanged("users", null, HospitalData.users);

        return new Response(StatusCode.CREATED, "Patient registered successfully");
    }
    
    public User getUserByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }
}
