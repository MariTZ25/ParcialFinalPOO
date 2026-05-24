/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate;
import org.json.JSONObject;


/**
 *
 * @author edangulo
 */
public class Hospitalization {
    
    private final String id;
    private Patient patient;
    private Doctor doctor;
    private LocalDate date;

    public String getId() {
        return id;
    }
    private String reason;
    private RoomType roomType;
    private String observations;
    private HospitalizationStatus status;

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public String getObservations() {
        return observations;
    }

    public HospitalizationStatus getStatus() {
        return status;
    }
    
    
    
    
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    //////////////////////////////////////////////
    public void setObservations(String observations) {
        this.observations = observations;
    }

    public void setStatus(HospitalizationStatus status) {
        this.status = status;
    }

    public Hospitalization(String id, Patient patient, Doctor doctor, LocalDate date, String reason, RoomType roomType, String observations) {
        this.id = id;
        this.patient = patient;
        patient.setHospitalization(this);
        this.doctor = doctor;
        doctor.addHospitalization(this);
        this.date = date;
        this.reason = reason;
        this.roomType = roomType;
        this.observations = observations;
        this.status = HospitalizationStatus.REQUESTED;
    }
    public Hospitalization(String id, Patient patient, Doctor doctor, LocalDate date, String reason, RoomType roomType, String observations, HospitalizationStatus hopsS) {
        this.id = id;
        this.patient = patient;
        patient.setHospitalization(this);
        this.doctor = doctor;
        doctor.addHospitalization(this);
        this.date = date;
        this.reason = reason;
        this.roomType = roomType;
        this.observations = observations;
        this.status = hopsS;
    }
    
    public JSONObject toJSON() {

    JSONObject obj = new JSONObject();

    obj.put("id", id);
    obj.put("patient", patient.getId());
    obj.put("doctor", doctor.getId());
    obj.put("date", date.toString());
    obj.put("reason", reason);
    obj.put("roomType", roomType);
    obj.put("observations", observations);
    obj.put("status", status);

    return obj;
}
    
}
