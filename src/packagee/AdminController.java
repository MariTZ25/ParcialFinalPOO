/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

import java.util.ArrayList;
import static packagee.HospitalData.appointments;
import static packagee.HospitalData.hospitalizations;
import static packagee.HospitalData.users;

/**
 *
 * @author AxelitoThunder
 */
public class AdminController {

    public ArrayList<User> getUsers() { 
        return users;
    }
    
    public ArrayList<Appointment> getAppointments() { 
        return appointments; 
    }
    
    public ArrayList<Hospitalization> getHospitalizations() { 
        return hospitalizations; 
    }
    
    public Response registerDoctor(
            long id,
            String username,
            String firstname,
            String lastname,
            String password,
            String confirmPassword,
            Specialty specialty,
            String licenceNumber,
            String assignedOffice
    ) {
        DoctorController doctorController = new DoctorController();

        return doctorController.registerDoctor(
                id,
                username,
                firstname,
                lastname,
                password,
                confirmPassword,
                specialty,
                licenceNumber,
                assignedOffice
        );
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
            long phone,
            String address
    ) {
        PatientController patientController = new PatientController();

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
                phone,
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

