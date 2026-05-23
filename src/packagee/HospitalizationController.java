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
public class HospitalizationController {
    public Response requestHospitalization(
            long patientId,
            long doctorId,
            String date,
            String reason,
            RoomType roomType,
            String observations
    ) {

        Patient patient = HospitalData.findPatientById(patientId);
        Doctor doctor = HospitalData.findDoctorById(doctorId);

        if (patient == null) {
            return new Response(StatusCode.NOT_FOUND, "Paciente no encontrado");
        }

        if (doctor == null) {
            return new Response(StatusCode.NOT_FOUND, "Doctor no encontrado");
        }

        LocalDate localDate;

        try {
            localDate = LocalDate.parse(date);
        } catch (Exception e) {
            return new Response(StatusCode.BAD_REQUEST, "Fecha inválida. Use AAAA-MM-DD");
        }

        String id = generateHospitalizationId(patientId);

        Hospitalization hospitalization = new Hospitalization(
                id,
                patient,
                doctor,
                localDate,
                reason,
                roomType,
                observations
        );

        HospitalData.hospitalizations.add(hospitalization);

        return new Response(StatusCode.CREATED, "Hospitalización solicitada correctamente", id);
    }

    public Response approveHospitalization(String hospitalizationId) {

        Hospitalization hospitalization = HospitalData.findHospitalizationById(hospitalizationId);

        if (hospitalization == null) {
            return new Response(StatusCode.NOT_FOUND, "Hospitalización no encontrada");
        }

        hospitalization.setStatus(HospitalizationStatus.ONGOING);

        return new Response(StatusCode.OK, "Hospitalización aprobada correctamente");
    }

    public Response cancelHospitalization(String hospitalizationId) {

        Hospitalization hospitalization = HospitalData.findHospitalizationById(hospitalizationId);

        if (hospitalization == null) {
            return new Response(StatusCode.NOT_FOUND, "Hospitalización no encontrada");
        }

        hospitalization.setStatus(HospitalizationStatus.CANCELED);

        return new Response(StatusCode.OK, "Hospitalización cancelada correctamente");
    }

    private String generateHospitalizationId(long patientId) {

        int count = 0;

        for (Hospitalization hospitalization : HospitalData.hospitalizations) {
            if (hospitalization.getId().contains(String.valueOf(patientId))) {
                count++;
            }
        }

        return String.format("H-%d-%04d", patientId, count);
    }
}
