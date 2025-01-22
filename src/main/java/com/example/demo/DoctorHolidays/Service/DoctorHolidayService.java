package com.example.demo.DoctorHolidays.Service;

import com.example.demo.DoctorHolidays.Entity.DoctorHoliday;
import com.example.demo.DoctorHolidays.Entity.Holiday;
import com.example.demo.DoctorHolidays.Repository.DoctorHolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorHolidayService {

    @Autowired
    private DoctorHolidayRepository repository;

    public Optional<DoctorHoliday> getDoctorHolidays(String doctorId, String clinicId) {
        return repository.findByDoctorIdAndClinicId(doctorId, clinicId);
    }

    public DoctorHoliday addHoliday(String doctorId, String clinicId, Holiday holiday) {
        Optional<DoctorHoliday> existingRecord = repository.findByDoctorIdAndClinicId(doctorId, clinicId);

        if (existingRecord.isPresent()) {
            DoctorHoliday doctorHoliday = existingRecord.get();

            // Check for duplicate holiday
            boolean holidayExists = doctorHoliday.getHolidays().stream()
                    .anyMatch(h -> h.getHolidayDate().equals(holiday.getHolidayDate()) &&
                            h.getReason().equals(holiday.getReason()));

            if (holidayExists) {
                throw new IllegalArgumentException("Holiday already exists for this date and reason.");
            }

            doctorHoliday.getHolidays().add(holiday);
            return repository.save(doctorHoliday);
        } else {
            // Create a new record
            DoctorHoliday newRecord = new DoctorHoliday();
            newRecord.setDoctorId(doctorId);
            newRecord.setClinicId(clinicId);
            newRecord.setHolidays(List.of(holiday));
            return repository.save(newRecord);
        }
    }

    public boolean deleteHoliday(String doctorId, String clinicId, Holiday holidayToDelete) {
        Optional<DoctorHoliday> existingRecord = repository.findByDoctorIdAndClinicId(doctorId, clinicId);

        if (existingRecord.isPresent()) {
            DoctorHoliday doctorHoliday = existingRecord.get();

            // Remove the holiday by date
            boolean isRemoved = doctorHoliday.getHolidays().removeIf(
                    h -> h.getHolidayDate().equals(holidayToDelete.getHolidayDate()));

            if (isRemoved) {
                repository.save(doctorHoliday); // Save the updated record
            }

            return isRemoved;
        }

        return false; // No matching record found
    }
}
