package com.example.demo.TimeSlots.Service;

import com.example.demo.TimeSlots.Entity.AvailableDays;
import com.example.demo.TimeSlots.Entity.DayEntry;
import com.example.demo.TimeSlots.Entity.TimeSlot;
import com.example.demo.TimeSlots.Repository.DoctorAvailabilityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorAvailabilityService {

    private final DoctorAvailabilityRepository repository;

    public DoctorAvailabilityService(DoctorAvailabilityRepository repository) {
        this.repository = repository;
    }

    // Find AvailableDays by doctorId and clinicId
    public Optional<AvailableDays> findByDoctorAndClinic(String doctorId, String clinicId) {
        return repository.findByDoctorIdAndClinicId(doctorId, clinicId);
    }

    // Update availability for a specific day (e.g., Monday)
    public boolean updateTimeSlot(AvailableDays updatedAvailableDays) {
        try {
            // Step 1: Fetch the existing document based on doctorId and clinicId
            Optional<AvailableDays> existingDataOpt = repository.findByDoctorIdAndClinicId(
                    updatedAvailableDays.getDoctorId(), updatedAvailableDays.getClinicId());

            if (existingDataOpt.isPresent()) {
                AvailableDays existingData = existingDataOpt.get();

                // Step 2: Find the day entry to update
                String updatedDay = updatedAvailableDays.getAvailableDays().get(0).getDay();
                DayEntry dayEntryToUpdate = existingData.getAvailableDays()
                        .stream()
                        .filter(day -> day.getDay().equals(updatedDay))
                        .findFirst()
                        .orElse(null);

                if (dayEntryToUpdate == null) {
                    return false; // Day entry not found
                }

                // Step 3: Find the TimeSlot to update using its _id
                String updatedTimeSlotId = updatedAvailableDays.getAvailableDays().get(0)
                        .getTimeSlots().get(0).get_id(); // Assuming one timeslot in the request

                TimeSlot timeSlotToUpdate = dayEntryToUpdate.getTimeSlots()
                        .stream()
                        .filter(slot -> slot.get_id().equals(updatedTimeSlotId))
                        .findFirst()
                        .orElse(null);

                if (timeSlotToUpdate == null) {
                    return false; // TimeSlot not found
                }

                // Step 4: Update only the fields provided in the request
                TimeSlot updatedTimeSlot = updatedAvailableDays.getAvailableDays().get(0).getTimeSlots().get(0);

                if (updatedTimeSlot.getStartTime() != null) {
                    timeSlotToUpdate.setStartTime(updatedTimeSlot.getStartTime());
                }
                if (updatedTimeSlot.getEndTime() != null) {
                    timeSlotToUpdate.setEndTime(updatedTimeSlot.getEndTime());
                }
                if (updatedTimeSlot.getStartDate() != null) {
                    timeSlotToUpdate.setStartDate(updatedTimeSlot.getStartDate());
                }
                if (updatedTimeSlot.getEndDate() != null) {
                    timeSlotToUpdate.setEndDate(updatedTimeSlot.getEndDate());
                }

                // Save the updated document back to the repository
                repository.save(existingData);
                return true;
            } else {
                return false; // Doctor and clinic not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
