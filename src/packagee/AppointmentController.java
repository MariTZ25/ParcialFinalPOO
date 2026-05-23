/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
/**
 *
 * @author Sahid
 */
public class AppointmentController {
    public Response requestAppointment(
            long patientId,
            long doctorId,
            Specialty specialty,
            String date,
            String time,
            String reason,
            boolean type
    ) {

        Patient patient = HospitalData.findPatientById(patientId);

        if (patient == null) {
            return new Response(StatusCode.NOT_FOUND, "Paciente no encontrado");
        }

        Doctor doctor = HospitalData.findDoctorById(doctorId);

        if (doctor == null) {
            return new Response(StatusCode.NOT_FOUND, "Doctor no encontrado");
        }

        if (doctor.getSpecialty() != specialty) {
            return new Response(StatusCode.BAD_REQUEST, "La especialidad no coincide con el doctor");
        }

        LocalDate localDate;
        LocalTime localTime;

        try {
            localDate = LocalDate.parse(date);
            localTime = LocalTime.parse(time);
        } catch (Exception e) {
            return new Response(StatusCode.BAD_REQUEST, "Fecha u hora inválida");
        }

        if (!(localTime.getMinute() == 0
                || localTime.getMinute() == 15
                || localTime.getMinute() == 30
                || localTime.getMinute() == 45)) {
            return new Response(StatusCode.BAD_REQUEST, "Los minutos deben ser 00, 15, 30 o 45");
        }

        LocalDateTime dateTime = LocalDateTime.of(localDate, localTime);

        if (!doctorAvailable(doctor, dateTime)) {
            return new Response(StatusCode.CONFLICT, "El doctor no está disponible");
        }

        String id = generateAppointmentId(patientId);

        Appointment appointment = new Appointment(id, patient, doctor, specialty, dateTime, reason, type);

        HospitalData.appointments.add(appointment);
        patient.addAppointment(appointment);

        return new Response(StatusCode.CREATED, "Cita solicitada correctamente", id);
    }

    public Response acceptAppointment(String appointmentId) {

        Appointment appointment = HospitalData.findAppointmentById(appointmentId);

        if (appointment == null) {
            return new Response(StatusCode.NOT_FOUND, "Cita no encontrada");
        }

        appointment.setStatus(AppointmentStatus.PENDING);

        return new Response(StatusCode.OK, "Cita aceptada correctamente");
    }

    public Response completeAppointment(String appointmentId) {

        Appointment appointment = HospitalData.findAppointmentById(appointmentId);

        if (appointment == null) {
            return new Response(StatusCode.NOT_FOUND, "Cita no encontrada");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);

        return new Response(StatusCode.OK, "Cita completada correctamente");
    }

    public Response cancelAppointment(String appointmentId) {

        Appointment appointment = HospitalData.findAppointmentById(appointmentId);

        if (appointment == null) {
            return new Response(StatusCode.NOT_FOUND, "Cita no encontrada");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            return new Response(StatusCode.BAD_REQUEST, "No se puede cancelar una cita completada");
        }

        appointment.setStatus(AppointmentStatus.CANCELED);

        return new Response(StatusCode.OK, "Cita cancelada correctamente");
    }

    public Response prescribeMedication(String appointmentId, Prescription prescription) {

        Appointment appointment = HospitalData.findAppointmentById(appointmentId);

        if (appointment == null) {
            return new Response(StatusCode.NOT_FOUND, "Cita no encontrada");
        }

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            return new Response(StatusCode.BAD_REQUEST, "Solo se puede prescribir en citas aceptadas");
        }

        appointment.addPrescription(prescription);

        return new Response(StatusCode.OK, "Medicamento prescrito correctamente");
    }

    private boolean doctorAvailable(Doctor doctor, LocalDateTime dateTime) {

        for (Appointment appointment : HospitalData.appointments) {

            if (appointment.getDoctor().getId() == doctor.getId()
                    && appointment.getDatetime().equals(dateTime)
                    && appointment.getStatus() != AppointmentStatus.CANCELED) {
                return false;
            }
        }

        return true;
    }

    private String generateAppointmentId(long patientId) {

        int count = 0;

        for (Appointment appointment : HospitalData.appointments) {
            if (appointment.getPatient().getId() == patientId) {
                count++;
            }
        }

        return String.format("A-%d-%04d", patientId, count);
    }
}
