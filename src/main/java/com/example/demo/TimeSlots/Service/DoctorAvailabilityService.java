package com.example.demo.TimeSlots.Service;

import com.example.demo.TimeSlots.Entity.AvailableDays;
import com.example.demo.TimeSlots.Entity.DayEntry;
import com.example.demo.TimeSlots.Entity.TimeSlot;
import com.example.demo.TimeSlots.Repository.DoctorAvailabilityRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

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
    public boolean addTimeSlots(AvailableDays newAvailableDays) {
        try {
            // Step 1: Fetch the existing document based on doctorId and clinicId
            Optional<AvailableDays> existingDataOpt = repository.findByDoctorIdAndClinicId(
                    newAvailableDays.getDoctorId(), newAvailableDays.getClinicId());

            if (existingDataOpt.isPresent()) {
                AvailableDays existingData = existingDataOpt.get();

                // Step 2: Iterate over the newAvailableDays to handle multiple days and time slots
                for (DayEntry newDayEntry : newAvailableDays.getAvailableDays()) {
                    String day = newDayEntry.getDay();

                    // Find the existing day entry or create a new one if it doesn't exist
                    DayEntry existingDayEntry = existingData.getAvailableDays()
                            .stream()
                            .filter(d -> d.getDay().equals(day))
                            .findFirst()
                            .orElse(null);

                    if (existingDayEntry == null) {
                        // If the day entry does not exist, create a new one
                        existingDayEntry = new DayEntry();
                        existingDayEntry.setDay(day);
                        existingDayEntry.setAvailable(true); // Assuming the day is available if new
                        existingDayEntry.setTimeSlots(new ArrayList<>()); // Initialize the timeSlots list
                        existingData.getAvailableDays().add(existingDayEntry);
                    }

                    // Step 3: Add new time slots to the day entry
                    for (TimeSlot newTimeSlot : newDayEntry.getTimeSlots()) {
                        // Generate a new ID in the format "timeslot_XXXXXX" if not provided
                        if (newTimeSlot.get_id() == null || newTimeSlot.get_id().isEmpty()) {
                            newTimeSlot.set_id(generateTimeSlotId()); // Use the helper method
                        }

                        // Add the new time slot to the day entry
                        existingDayEntry.getTimeSlots().add(newTimeSlot);
                    }
                }

                // Step 4: Save the updated document back to the repository
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
    private String generateTimeSlotId() {
        Random random = new Random();
        int randomNumber = random.nextInt(1000000); // Generates a number between 0 and 999999
        return String.format("timeslot_%06d", randomNumber); // Formats the number as 6 digits
    }
}