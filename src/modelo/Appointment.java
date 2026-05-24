/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author edangulo
 */
public class Appointment {
    
    private final String id;
    private Patient patient;
    private Doctor doctor;
    
    private Specialty specialty;
    private LocalDateTime datetime;
    private String reason;
    private boolean type;
    private ArrayList<Prescription> prescriptions;
    private AppointmentStatus status;
    private String diagnosis;
    private String observations;
    private String recommendedTreatment;
    private String followUp;
   
    
    
    
    
    
    
    public void setReason(String reason) {
        this.reason = reason;
    }


    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public void setRecommendedTreatment(String recommendedTreatment) {
        this.recommendedTreatment = recommendedTreatment;
    }

    public void setFollowUp(String followUp) {
        this.followUp = followUp;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public void setPrescriptions(ArrayList<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }
    
    ///////
        public Appointment(String id, Patient patient, Doctor doctor, Specialty specialty, LocalDateTime datetime, String reason, boolean type) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.specialty = specialty;
        this.datetime = datetime;
        this.reason = reason;
        this.type = type;
        this.status = AppointmentStatus.REQUESTED;
        this.prescriptions = new ArrayList<>();
        
        ////Hace parte de relación con los métodos de doctor y patient
        patient.addAppointment(this);
        
        if(doctor != null){
        doctor.addAppointment(this);
        }
        
    }
    ////////

    public String getId() {
        return id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public boolean isType() {
        return type;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getReason() {
        return reason;
    }

    public ArrayList<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getObservations() {
        return observations;
    }

    public String getRecommendedTreatment() {
        return recommendedTreatment;
    }

    public String getFollowUp() {
        return followUp;
    }
    
    
   public JSONObject toJSON() {

    JSONObject obj = new JSONObject();

    obj.put("id", id);
    obj.put("patient", patient.getId());
    obj.put("doctor", doctor.getId());
    obj.put("specialty", specialty);
    obj.put("datetime", datetime.toString());
    obj.put("reason", reason);
    obj.put("type", type);
    obj.put("status", status);

    obj.put("diagnosis", diagnosis);
    obj.put("observations", observations);
    obj.put("recommendedTreatment", recommendedTreatment);
    obj.put("followUp", followUp);

    return obj;
}

    public boolean addPrescription(Prescription prescrip) {
        return this.prescriptions.add(prescrip);
    }
    
    
}
