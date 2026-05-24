/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author edangulo
 */
public class Doctor extends User {
    
    private Specialty specialty;
    private String licenceNumber;
    private String assignedOffice;
    private ArrayList<Appointment> appointments ;
    private ArrayList<Hospitalization> hospitalizations;

    public Doctor(long id, String username, String firstname, String lastname, String password, Specialty specialty, String licenceNumber, String assignedOffice) {
        super(id, username, firstname, lastname, password);
        hospitalizations = new ArrayList<>();
        appointments= new ArrayList<>();
        this.specialty = specialty;
        this.licenceNumber = licenceNumber;
        this.assignedOffice = assignedOffice;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public String getAssignedOffice() {
        return assignedOffice;
    }

    public ArrayList<Hospitalization> getHospitalizations() {
        return hospitalizations;
    }
    
    
    
    ////////
    
    public boolean addHospitalization(Hospitalization hosp){
        return hospitalizations.add(hosp);
    }
    
    ///////
    

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public void setAssignedOffice(String assignedOffice) {
        this.assignedOffice = assignedOffice;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void setHospitalizations(ArrayList<Hospitalization> hospitalizations) {
        this.hospitalizations = hospitalizations;
    }
    public boolean addAppointment(Appointment apptmnt){
    return appointments.add(apptmnt);
}
    
    @Override
public JSONObject toJSON() {

    JSONObject obj = super.toJSON();

    obj.put("specialty", specialty);
    obj.put("licenceNumber", licenceNumber);
    obj.put("assignedOffice", assignedOffice);

    return obj;
}
}
