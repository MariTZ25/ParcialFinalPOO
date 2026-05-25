/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import modelo.Appointment;
import modelo.AppointmentStatus;
import modelo.Doctor;
import modelo.Patient;
import modelo.Prescription;
import modelo.Specialty;
import modelo.User;
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
        Appointment appointment = findAppointmentEverywhere(appointmentId);

        if (appointment == null) {
            return new Response(StatusCode.NOT_FOUND, "Cita no encontrada");
        }
        if (appointment.getStatus() != AppointmentStatus.REQUESTED) {
            return new Response(StatusCode.BAD_REQUEST, "Solo se pueden aceptar citas en estado REQUESTED");
        }

        appointment.setStatus(AppointmentStatus.PENDING);
        return new Response(StatusCode.OK, "Cita aceptada correctamente");
}

    public Response completeAppointment(String appointmentId) {

        Appointment appointment = findAppointmentEverywhere(appointmentId);

        if (appointment == null) {
            return new Response(StatusCode.NOT_FOUND, "Cita no encontrada");
        }
        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            return new Response(StatusCode.BAD_REQUEST, "Solo se pueden completar citas aceptadas");
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

        Appointment appointment = findAppointmentEverywhere(appointmentId);

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
    
    public Response rescheduleAppointment(String appointmentId, String newTime, String reason) {
        Appointment appointment = findAppointmentEverywhere(appointmentId);
        if (appointment == null) {
            return new Response(StatusCode.NOT_FOUND, "Cita no encontrada");
        }
        LocalTime localTime;
        try {
            localTime = LocalTime.parse(newTime);
        } catch (Exception e) {
            return new Response(StatusCode.BAD_REQUEST, "Hora inválida. Use hh:mm");
        }
        int minutes = localTime.getMinute();
        if (minutes != 0 && minutes != 15 && minutes != 30 && minutes != 45) {
            return new Response(StatusCode.BAD_REQUEST, "Los minutos deben ser 00, 15, 30 o 45");
        }
        LocalDateTime newDateTime = LocalDateTime.of(appointment.getDatetime().toLocalDate(), localTime);

        if (!doctorAvailable(appointment.getDoctor(), newDateTime)) {
            return new Response(StatusCode.CONFLICT, "El doctor no está disponible en esa hora");
        }
        appointment.setDatetime(newDateTime);
        appointment.setReason(appointment.getReason() + " | Reprogramación: " + reason);
        return new Response(StatusCode.OK, "Cita reprogramada correctamente");
    }
    
    private Appointment findAppointmentEverywhere(String appointmentId) {
        for (Appointment appointment : HospitalData.appointments) {
            if (appointment.getId().equals(appointmentId)) {
                return appointment;
            }
        }

        for (User user : HospitalData.users) {
            if (user instanceof Doctor) {
                Doctor doctor = (Doctor) user;
                for (Appointment appointment : doctor.getAppointments()) {
                    if (appointment.getId().equals(appointmentId)) {
                        return appointment;
                    }
                }
            }

            if (user instanceof Patient) {
                Patient patient = (Patient) user;
                for (Appointment appointment : patient.getAppointments()) {
                    if (appointment.getId().equals(appointmentId)) {
                        return appointment;
                    }
                }
            }
        }

        return null;
    }
    
    public Response prescribeMedications(String[][] medicationsData) {
        if (medicationsData == null || medicationsData.length == 0) {
            return new Response(StatusCode.BAD_REQUEST, "No hay medicamentos para prescribir");
        }

        for (String[] row : medicationsData) {
            String appointmentId = row[0];
            Appointment appointment = findAppointmentEverywhere(appointmentId);

            if (appointment == null) {
                return new Response(StatusCode.NOT_FOUND, "Cita no encontrada: " + appointmentId);
            }
            if (appointment.getStatus() != AppointmentStatus.PENDING) {
                return new Response(StatusCode.BAD_REQUEST, "Solo se puede prescribir en citas aceptadas");
            }
            double dose;
            int treatmentDuration;
            int frecuency;
            try {
                dose = Double.parseDouble(row[2]);
                treatmentDuration = Integer.parseInt(row[4]);
                frecuency = Integer.parseInt(row[6]);
            } catch (NumberFormatException e) {
                return new Response(StatusCode.BAD_REQUEST, "Dosis, duración y frecuencia deben ser numéricas");
            }
            new Prescription(appointment, row[1], dose, row[3], treatmentDuration, row[5], frecuency);
        }
        return new Response(StatusCode.OK, "Medicamentos prescritos correctamente");
    }
}
