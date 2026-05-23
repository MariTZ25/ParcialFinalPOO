/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

import java.time.LocalDate;
/**
 *
 * @author Sahid
 */
public class PatientController {
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
