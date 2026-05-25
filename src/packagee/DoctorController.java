/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

import java.util.ArrayList;
import modelo.Appointment;
import modelo.Doctor;
import modelo.Hospitalization;
import modelo.Specialty;
import modelo.User;
import static packagee.HospitalData.appointments;
import static packagee.HospitalData.hospitalizations;
import static packagee.HospitalData.users;

/**
 *
 * @author Sahid
 */
public class DoctorController {
    
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

        if (id <= 0 || String.valueOf(id).length() != 12) {
            return new Response(StatusCode.BAD_REQUEST, "El ID debe tener 12 dígitos");
        }

        if (HospitalData.findDoctorById(id) != null || HospitalData.findPatientById(id) != null) {
            return new Response(StatusCode.CONFLICT, "El ID ya existe");
        }

        if (HospitalData.usernameExists(username)) {
            return new Response(StatusCode.CONFLICT, "El usuario ya existe");
        }

        if (!password.equals(confirmPassword)) {
            return new Response(StatusCode.BAD_REQUEST, "Las contraseñas no coinciden");
        }

        if (!licenceNumber.matches("^L-\\d{10} MTL$")) {
            return new Response(StatusCode.BAD_REQUEST, "Licencia inválida. Formato: L-XXXXXXXXXX MTL");
        }

        if (!assignedOffice.matches("^O-\\d{3}$")) {
            return new Response(StatusCode.BAD_REQUEST, "Oficina inválida. Formato: O-XXX");
        }

        Doctor doctor = new Doctor(id, username, firstname, lastname, password, specialty, licenceNumber, assignedOffice);
        HospitalData.users.add(doctor);

        return new Response(StatusCode.CREATED, "Doctor registrado correctamente");
    }

    public Response updateDoctor(
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

        Doctor doctor = HospitalData.findDoctorById(id);

        if (doctor == null) {
            return new Response(StatusCode.NOT_FOUND, "Doctor no encontrado");
        }

        if (!password.equals(confirmPassword)) {
            return new Response(StatusCode.BAD_REQUEST, "Las contraseñas no coinciden");
        }

        if (!licenceNumber.matches("^L-\\d{10} MTL$")) {
            return new Response(StatusCode.BAD_REQUEST, "Licencia inválida");
        }

        if (!assignedOffice.matches("^O-\\d{3}$")) {
            return new Response(StatusCode.BAD_REQUEST, "Oficina inválida");
        }

        doctor.setUsername(username);
        doctor.setFirstname(firstname);
        doctor.setLastname(lastname);
        doctor.setPassword(password);
        doctor.setSpecialty(specialty);
        doctor.setLicenceNumber(licenceNumber);
        doctor.setAssignedOffice(assignedOffice);

        return new Response(StatusCode.OK, "Doctor actualizado correctamente");
    }
    
    public Response validateMedicationToAdd(String appointmentId, String medicationName, String doseText, String administrationRoute, String treatmentDurationText, String additionalInstructions, String frequencyText) {
        if (appointmentId == null || appointmentId.equals("Select one") || medicationName.trim().isEmpty() || doseText.trim().isEmpty() || administrationRoute.trim().isEmpty() || treatmentDurationText.trim().isEmpty() || additionalInstructions.trim().isEmpty() || frequencyText.trim().isEmpty()) {
            return new Response(StatusCode.BAD_REQUEST, "Complete todos los campos");
        }

        try {
            Double.parseDouble(doseText);
            Integer.parseInt(treatmentDurationText);
            Integer.parseInt(frequencyText);
        } catch (NumberFormatException e) {
            return new Response(StatusCode.BAD_REQUEST, "Dose, Treatment duration and frecuency they must be numerical");
        }

        String data = appointmentId + ";" + medicationName + ";" + doseText + ";" + administrationRoute + ";" + treatmentDurationText + ";" + additionalInstructions + ";" + frequencyText;
        return new Response(StatusCode.OK, "Medicamento agregado a la tabla", data);
    }
    
}
