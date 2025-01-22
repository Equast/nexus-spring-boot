package com.example.demo.DoctorHolidays.Controller;

import com.example.demo.DoctorHolidays.Entity.DoctorHoliday;
import com.example.demo.DoctorHolidays.Entity.Holiday;
import com.example.demo.DoctorHolidays.Service.DoctorHolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/doctor-holidays")
public class DoctorHolidayController {

    @Autowired
    private DoctorHolidayService service;

    @GetMapping
    public ResponseEntity<?> getDoctorHolidays(@RequestParam String doctorId, @RequestParam String clinicId) {
        if (doctorId == null || clinicId == null) {
            return new ResponseEntity<>("Missing doctorId or clinicId query parameters.", HttpStatus.BAD_REQUEST);
        }

        Optional<DoctorHoliday> doctorHoliday = service.getDoctorHolidays(doctorId, clinicId);
        if (doctorHoliday.isPresent()) {
            return new ResponseEntity<>(doctorHoliday.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("No holidays found for this doctor and clinic.", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/add")
    public ResponseEntity<?> addHoliday(@RequestBody DoctorHoliday request) {
        try {
            if (request.getDoctorId() == null || request.getClinicId() == null || request.getHolidays() == null) {
                return new ResponseEntity<>("Invalid request data.", HttpStatus.BAD_REQUEST);
            }

            Holiday holiday = request.getHolidays().get(0);
            if (holiday.getHolidayDate() == null || holiday.getReason() == null) {
                return new ResponseEntity<>("Holiday date or reason is missing.", HttpStatus.BAD_REQUEST);
            }

            DoctorHoliday updatedRecord = service.addHoliday(request.getDoctorId(), request.getClinicId(), holiday);
            return new ResponseEntity<>(updatedRecord, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Server error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteHoliday(@RequestBody DoctorHoliday request) {
        try {
            if (request.getDoctorId() == null || request.getClinicId() == null || request.getHolidays() == null) {
                return new ResponseEntity<>("Invalid request data.", HttpStatus.BAD_REQUEST);
            }

            Holiday holidayToDelete = request.getHolidays().get(0);
            if (holidayToDelete.getHolidayDate() == null) {
                return new ResponseEntity<>("Holiday date is missing.", HttpStatus.BAD_REQUEST);
            }

            boolean isDeleted = service.deleteHoliday(request.getDoctorId(), request.getClinicId(), holidayToDelete);

            if (isDeleted) {
                return new ResponseEntity<>("Holiday deleted successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Holiday not found.", HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity<>("Server error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
