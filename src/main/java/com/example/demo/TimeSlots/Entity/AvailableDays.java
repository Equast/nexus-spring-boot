package com.example.demo.TimeSlots.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "AllDoctorsADS")
public class AvailableDays {

    @Id
    private String id;                 // MongoDB ID
    private String doctorId;           // Doctor ID
    private String clinicId;           // Clinic ID
    private List<DayEntry> availableDays; // List of days' availability

    public AvailableDays() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public List<DayEntry> getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(List<DayEntry> availableDays) {
        this.availableDays = availableDays;
    }
}

