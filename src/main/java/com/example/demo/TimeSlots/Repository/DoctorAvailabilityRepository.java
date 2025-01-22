package com.example.demo.TimeSlots.Repository;

import com.example.demo.TimeSlots.Entity.AvailableDays;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorAvailabilityRepository extends MongoRepository<AvailableDays, String> {
    Optional<AvailableDays> findByDoctorIdAndClinicId(String doctorId, String clinicId);
}
