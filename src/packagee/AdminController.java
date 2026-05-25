/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

import java.util.ArrayList;
import modelo.Appointment;
import modelo.Doctor;
import modelo.Hospitalization;
import modelo.Patient;
import modelo.Specialty;
import modelo.User;

/**
 *
 * @author AxelitoThunder
 */
public class AdminController {

    private ArrayList<User> users;
    private ArrayList<Appointment> appointments;
    private ArrayList<Hospitalization> hospitalizations;
    
    //Constructor
    public AdminController(ArrayList<User> users, ArrayList<Appointment> appointments, ArrayList<Hospitalization> hospitalizations) {
        this.users = users;
        this.appointments = appointments;
        this.hospitalizations = hospitalizations;
    }
    
    //Getters
    public ArrayList<User> getUsers() { 
        return users;
    }
    
    public ArrayList<Appointment> getAppointments() { 
        return appointments; 
    }
    
    public ArrayList<Hospitalization> getHospitalizations() { 
        return hospitalizations; 
    }
    
    public Response registerDoctor(String firstname, String lastname, String idText, String spec, String licenseNumber, String assignedOffice, String username, String password, String comPassword) {
        if (firstname.trim().isEmpty() || lastname.trim().isEmpty() || idText.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty() || comPassword.trim().isEmpty() || licenseNumber.trim().isEmpty() || assignedOffice.trim().isEmpty()) {
            return new Response(StatusCode.BAD_REQUEST, "Complete all required fields");
        }

        long id;

        try {
            id = Long.parseLong(idText);
        } catch (NumberFormatException e) {
            return new Response(StatusCode.BAD_REQUEST, "ID must be numeric");
        }

        if (id <= 0 || idText.length() != 12) {
            return new Response(StatusCode.BAD_REQUEST, "ID must have exactly 12 digits");
        }
        if (spec.equals("Select one")) {
            return new Response(StatusCode.BAD_REQUEST, "Select a specialty");
        }
        if (!password.equals(comPassword)) {
            return new Response(StatusCode.BAD_REQUEST, "Passwords do not match");
        }

        for (User u : users) {
            if (u.getId() == id) {
                return new Response(StatusCode.CONFLICT, "ID already exists");
            }
            if (u.getUsername().equals(username)) {
                return new Response(StatusCode.CONFLICT, "Username already exists");
            }
        }

        if (!licenseNumber.matches("^L-\\d{10} MTL$")) {
            return new Response(StatusCode.BAD_REQUEST, "Invalid license format. Use L-XXXXXXXXXX MTL");
        }
        if (!assignedOffice.matches("^O-\\d{3}$")) {
            return new Response(StatusCode.BAD_REQUEST, "Invalid office format. Use O-XXX");
        }

        Specialty specialty = Specialty.fromDisplayName(spec);
        users.add(new Doctor(id, username, firstname, lastname, password, specialty, licenseNumber, assignedOffice));
        return new Response(StatusCode.CREATED, "Doctor registered successfully");
    }
    
    public Doctor findDoctorById(String idText) {
        if (idText.equals("Select one")) {
            return null;
        }
        long idDoctor;
        try {
            idDoctor = Long.parseLong(idText);
        } catch (NumberFormatException e) {
            return null;
        }
        for (User use : users) {
            if (use instanceof Doctor && use.getId() == idDoctor) {
                return (Doctor) use;
            }
        }
        return null;
    }
    
    public Patient findPatientById(String idText) {
        if (idText.equals("Select one")) {
            return null;
        }
        long idPatient;
        try {
            idPatient = Long.parseLong(idText);
        } catch (NumberFormatException e) {
            return null;
        }
        for (User use : users) {
            if (use instanceof Patient && use.getId() == idPatient) {
                return (Patient) use;
            }
        }
        return null;
    }
    
    public Response registerPatient(
            long id,
            String username,
            String firstname,
            String lastname,
            String password,
            String confirmPassword,
            String email,
            String birthdate,
            boolean gender,
            String phoneText,
            String address
    ) {
        
        PatientController patientController = new PatientController(users, appointments, hospitalizations);

        return patientController.registerPatient(
                id,
                username,
                firstname,
                lastname,
                password,
                confirmPassword,
                email,
                birthdate,
                gender,
                phoneText,
                address
        );
    }

    public Response getPatientById(long id) {
        Patient patient = HospitalData.findPatientById(id);

        if (patient == null) {
            return new Response(StatusCode.NOT_FOUND, "Paciente no encontrado");
        }

        return new Response(StatusCode.OK, "Paciente encontrado", String.valueOf(patient.getId()));
    }

    public Response getDoctorById(long id) {
        Doctor doctor = HospitalData.findDoctorById(id);

        if (doctor == null) {
            return new Response(StatusCode.NOT_FOUND, "Doctor no encontrado");
        }

        return new Response(StatusCode.OK, "Doctor encontrado", String.valueOf(doctor.getId()));
    }
}

