/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

/**
 *
 * @author Sahid
 */
public class DoctorController {
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
}
