package com.example.demo.TimeSlots.Controller;

import com.example.demo.TimeSlots.Entity.AvailableDays;
import com.example.demo.TimeSlots.Service.DoctorAvailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/doctor")
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService doctorAvailabilityService;

    public DoctorAvailabilityController(DoctorAvailabilityService doctorAvailabilityService) {
        this.doctorAvailabilityService = doctorAvailabilityService;
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<AvailableDays> getDoctorAvailability(
            @PathVariable String doctorId,
            @RequestParam String clinicId) {
        if (clinicId == null || clinicId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<AvailableDays> doctorData = doctorAvailabilityService.findByDoctorAndClinic(doctorId, clinicId);
        return doctorData.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<?> updateTimeSlot(@RequestBody AvailableDays updatedAvailableDays) {
        try {
            boolean updated = doctorAvailabilityService.updateTimeSlot(updatedAvailableDays);
            if (updated) {
                return ResponseEntity.ok("TimeSlot updated successfully");
            } else {
                return ResponseEntity.status(404).body("Doctor, clinic, day, or timeslot not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }
    @PostMapping
    public ResponseEntity<?> addTimeSlots(@RequestBody AvailableDays newAvailableDays) {
        if (newAvailableDays.getDoctorId() == null || newAvailableDays.getClinicId() == null) {
            return ResponseEntity.badRequest().body("Doctor ID and Clinic ID are required");
        }

        boolean isAdded = doctorAvailabilityService.addTimeSlots(newAvailableDays);
        if (isAdded) {
            return ResponseEntity.ok("TimeSlots added successfully");
        } else {
            return ResponseEntity.status(404).body("Doctor or clinic not found");
        }
    }

}
