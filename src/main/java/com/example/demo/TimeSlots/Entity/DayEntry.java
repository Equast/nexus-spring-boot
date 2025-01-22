package com.example.demo.TimeSlots.Entity;

import java.util.List;

public class DayEntry {
    private String day;               // Day of the week (e.g., Monday, Tuesday)
    private boolean isAvailable;      // Availability status
    private List<TimeSlot> timeSlots; // List of time slots for the day

    public DayEntry() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }
}

