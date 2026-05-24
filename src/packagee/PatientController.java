/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import modelo.Appointment;
import modelo.AppointmentStatus;
import modelo.Doctor;
import modelo.Hospitalization;
import modelo.Patient;
import modelo.RoomType;
import modelo.Specialty;
import modelo.User;

/**
 *
 * @author Sahid
 */
public class PatientController {
    
    private ArrayList<User> users;
    private ArrayList<Appointment> appointments;
    private ArrayList<Hospitalization> hospitalizations;
    
    public ArrayList<User> getUsers() { 
        return users;
    }
    
    public ArrayList<Appointment> getAppointments() { 
        return appointments; 
    }
    
    public ArrayList<Hospitalization> getHospitalizations() { 
        return hospitalizations; 
    }
    
    public Response createAppointment(String dateText, String timeText, String reason, boolean isSpecialtyMode, String selectedValue, Patient patient) {
        LocalDate date;
        LocalTime time;

        try {
            date = LocalDate.of(Integer.parseInt(dateText.substring(0, 4)), Integer.parseInt(dateText.substring(5, 7)), Integer.parseInt(dateText.substring(8)));
            time = LocalTime.of(Integer.parseInt(timeText.substring(0, 2)), Integer.parseInt(timeText.substring(3)));
        } catch (Exception e) {
            return new Response(StatusCode.BAD_REQUEST, "Fecha u hora inválida");
        }

        LocalDateTime dateTime = LocalDateTime.of(date, time);

        Doctor doctor = null;
        Specialty specialty = null;

        if (isSpecialtyMode) {
            try {
                specialty = Specialty.valueOf(selectedValue.replace(" & ", "_").toUpperCase());
            } catch (Exception e) {
                return new Response(StatusCode.BAD_REQUEST, "Especialidad inválida");
            }
            
        } else {
            for (User u : users) {
                if (u instanceof Doctor &&
                    (u.getFirstname() + " " + u.getLastname()).equals(selectedValue)) {
                    doctor = (Doctor) u;
                    break;
                }
            }

            if (doctor == null) {
                return new Response(StatusCode.NOT_FOUND, "Doctor no encontrado");
            }
        }

        Appointment ap = new Appointment(reason, patient, doctor, specialty, dateTime, reason, false);
        appointments.add(ap);

        return new Response(StatusCode.CREATED, "Cita creada correctamente");
    }
    
    public Response cancelAppointment(String appointmentId, String observations) {

        for (Appointment ap : appointments) {
            if (ap.getId().equals(appointmentId)) {
                ap.setStatus(AppointmentStatus.CANCELED);
                return new Response(StatusCode.OK, "Cita cancelada");
            }
        }
        return new Response(StatusCode.NOT_FOUND, "Cita no encontrada");
    }
    
    public Response registerPatient(long id, String username,String firstname, String lastname, String password, String confirmPassword, String email, String birthdate, boolean gender, long phone, String address) {
        if (id <= 0 || String.valueOf(id).length() != 12) {
            return new Response(StatusCode.BAD_REQUEST, "El ID debe ser mayor que 0 y tener 12 dígitos");
        }

        if (HospitalData.findPatientById(id) != null || HospitalData.findDoctorById(id) != null) {
            return new Response(StatusCode.CONFLICT, "El ID ya existe");
        }

        if (HospitalData.usernameExists(username)) {
            return new Response(StatusCode.CONFLICT, "El usuario ya existe");
        }

        if (!password.equals(confirmPassword)) {
            return new Response(StatusCode.BAD_REQUEST, "Las contraseñas no coinciden");
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.com$")) {
            return new Response(StatusCode.BAD_REQUEST, "Email inválido");
        }

        if (String.valueOf(phone).length() != 10) {
            return new Response(StatusCode.BAD_REQUEST, "El teléfono debe tener 10 dígitos");
        }

        LocalDate date;

        try {
            date = LocalDate.parse(birthdate);
        } catch (Exception e) {
            return new Response(StatusCode.BAD_REQUEST, "Fecha inválida. Use AAAA-MM-DD");
        }

        Patient patient = new Patient(id, username, firstname, lastname, password, email, date, gender, phone, address);
        HospitalData.users.add(patient);

        return new Response(StatusCode.CREATED, "Paciente registrado correctamente");
    }

    public Response createHospitalization(String reason, String doctorSelected, String dateText, String roomTypeText, String observations, Patient patient) {
        Doctor doctor = null;
        for (User u : users) {
            if (u instanceof Doctor &&
                (u.getFirstname() + " " + u.getLastname()).equals(doctorSelected)) {
                doctor = (Doctor) u;
                break;
            }
        }
        if (doctor == null) {
            return new Response(StatusCode.NOT_FOUND, "Doctor no encontrado");
        }
        LocalDate date;
        try {
            date = LocalDate.of(Integer.parseInt(dateText.substring(0, 4)), Integer.parseInt(dateText.substring(5, 7)), Integer.parseInt(dateText.substring(8)));
        } catch (Exception e) {
            return new Response(StatusCode.BAD_REQUEST, "Fecha inválida");
        }
        RoomType roomType;
        try {
            roomType = RoomType.valueOf(roomTypeText.toUpperCase());
        } catch (Exception e) {
            return new Response(StatusCode.BAD_REQUEST, "Tipo de habitación inválido");
        }
        
        Hospitalization h = new Hospitalization(observations, patient, doctor, date, reason, roomType,observations);
        hospitalizations.add(h);
        return new Response(StatusCode.CREATED, "Hospitalización creada");
    }
       
    public Response updatePatient(
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

        Patient patient = HospitalData.findPatientById(id);

        if (patient == null) {
            return new Response(StatusCode.NOT_FOUND, "Paciente no encontrado");
        }

        if (!password.equals(confirmPassword)) {
            return new Response(StatusCode.BAD_REQUEST, "Las contraseñas no coinciden");
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.com$")) {
            return new Response(StatusCode.BAD_REQUEST, "Email inválido");
        }

        if (String.valueOf(phone).length() != 10) {
            return new Response(StatusCode.BAD_REQUEST, "El teléfono debe tener 10 dígitos");
        }

        LocalDate date;

        try {
            date = LocalDate.parse(birthdate);
        } catch (Exception e) {
            return new Response(StatusCode.BAD_REQUEST, "Fecha inválida");
        }

        patient.setUsername(username);
        patient.setFirstname(firstname);
        patient.setLastname(lastname);
        patient.setPassword(password);
        patient.setEmail(email);
        patient.setBirthdate(date);
        patient.setGender(gender);
        patient.setPhone(phone);
        patient.setAddress(address);

        return new Response(StatusCode.OK, "Paciente actualizado correctamente");
    }
    
    
}


