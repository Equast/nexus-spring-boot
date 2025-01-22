package com.example.demo.DoctorHolidays.Entity;

import java.util.Date;

public class Holiday {

    private Date holidayDate;
    private String reason;
    private String frequency;

    public Holiday() {
    }

    public Date getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(Date holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
