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

    public PatientController(ArrayList<User> users, ArrayList<Appointment> appointments, ArrayList<Hospitalization> hospitalizations) {
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
    
    public Response createAppointment(String dateText, String timeText, String reason, boolean isSpecialtyMode, String selectedValue, boolean type, Patient patient) {
        LocalDate date;
        LocalTime time;

        try {
            date = LocalDate.of(Integer.parseInt(dateText.substring(0, 4)), Integer.parseInt(dateText.substring(5, 7)), Integer.parseInt(dateText.substring(8)));
            time = LocalTime.of(Integer.parseInt(timeText.substring(0, 2)), Integer.parseInt(timeText.substring(3)));
        } catch (Exception e) {
            return new Response(StatusCode.BAD_REQUEST, "Fecha u hora inválida");
        }
        
        //Verify 0, 15, 30, 45
        int minutes = time.getMinute();

        if (minutes != 0 && minutes != 15 && minutes != 30 && minutes != 45) {
            return new Response(StatusCode.BAD_REQUEST, "Los minutos deben ser 00, 15, 30 o 45");
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

            for (User u : users) {
                if (u instanceof Doctor) {
                    Doctor d = (Doctor) u;
                    if (d.getSpecialty() == specialty && doctorAvailable(d, dateTime)) {
                        doctor = d;
                        break;
                    }
                }
            }

            if (doctor == null) {
                return new Response(StatusCode.CONFLICT, "No hay doctores disponibles");
            }

        } else {
            for (User u : users) {
                if (u instanceof Doctor && (u.getFirstname() + " " + u.getLastname()).equals(selectedValue)) {
                    doctor = (Doctor) u;
                    specialty = doctor.getSpecialty();
                    break;
                }
            }

            if (doctor == null) {
                return new Response(StatusCode.NOT_FOUND, "Doctor no encontrado");
            }

            if (!doctorAvailable(doctor, dateTime)) {
                return new Response(StatusCode.CONFLICT, "El doctor no tiene disponibilidad");
            }
        }
        
        String id = generateAppointmentId(patient);

        Appointment ap = new Appointment(id, patient, doctor, specialty, dateTime, reason, type);
        appointments.add(ap);
        HospitalData.fireTableChanged("appointments", null, HospitalData.appointments);

        return new Response(StatusCode.CREATED, "Cita creada correctamente");
    }
    
    private String generateAppointmentId(Patient patient) {
        int count = 0;
        for (Appointment ap : appointments) {
            if (ap.getPatient().getId() == patient.getId()) {
                count++;
            }
        }
        return "A-" + patient.getId() + "-" + String.format("%04d", count);
    }
    
    public Response cancelAppointment(String appointmentId, String observations) {

        for (Appointment ap : appointments) {
            if (ap.getId().equals(appointmentId)) {
                if (ap.getStatus() == AppointmentStatus.COMPLETED) {
                    return new Response(StatusCode.BAD_REQUEST, "No se puede cancelar una cita completada");
                }
                ap.setStatus(AppointmentStatus.CANCELED);
                return new Response(StatusCode.OK, "Cita cancelada");
            }
        }
        return new Response(StatusCode.NOT_FOUND, "Cita no encontrada");
    }
    
    public Response registerPatient(long id, String username,String firstname, String lastname, String password, String confirmPassword, String email, String birthdate, boolean gender, String phoneText, String address) {
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

        long phone;
        try {
            phone = Long.parseLong(phoneText);
        } catch (Exception e) {
            return new Response(StatusCode.BAD_REQUEST,"El teléfono solo debe contener números");
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
        
        HospitalData.fireTableChanged("users", null, HospitalData.users);

        return new Response(StatusCode.CREATED, "Paciente registrado correctamente");
    }

    public ArrayList<Object[]> getPatientAppointments(Patient patient) {
        ArrayList<Object[]> rows = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getPatient().getId() == patient.getId()) {
                String doctorName = "-";
                if (a.getDoctor() != null) {
                    doctorName = a.getDoctor().getFirstname() + " " + a.getDoctor().getLastname();
                }
                String specialty = "-";
                if (a.getSpecialty() != null) {
                    specialty = a.getSpecialty().name();
                }
                rows.add(new Object[]{a.getId(), a.getDatetime().toString(), doctorName, specialty, a.isType() ? "In-person" : "Remote", a.getStatus().name()});
            }
        }
        //Sort most recents
        rows.sort((a, b) -> ((String) b[1]).compareTo((String) a[1]));
        return rows;
    }
       
    public ArrayList<String> getSpecialties() {
        ArrayList<String> specialties = new ArrayList<>();
        for (Specialty spec : Specialty.values()) {
            specialties.add(spec.toString().replaceAll("_", " & "));
        }
        return specialties;
    }
    
    public ArrayList<String> getDoctors() {
        ArrayList<String> doctors = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Doctor) {
                doctors.add(user.getFirstname() + " " + user.getLastname());
                
        HospitalData.fireTableChanged("users", null, HospitalData.users);
            }
        }
        return doctors;
    }
    
    public Response updatePatient(long id, String username, String firstname, String lastname, String password, String confirmPassword, String email, String birthdate, boolean gender, long phone, String address) {
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
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getId() != id) {
                return new Response(StatusCode.CONFLICT, "El username ya existe");
            }
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
    
    private boolean doctorAvailable(Doctor doctor, LocalDateTime datetime) {
        for (Appointment ap : appointments) {
            if (ap.getDoctor() != null && ap.getDoctor().getId() == doctor.getId() && ap.getDatetime().equals(datetime) && ap.getStatus() != AppointmentStatus.CANCELED) {
                return false;
            }
        }
        return true;
    }
    
}


