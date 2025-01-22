package com.example.demo.DoctorHolidays.Repository;

import com.example.demo.DoctorHolidays.Entity.DoctorHoliday;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DoctorHolidayRepository extends MongoRepository<DoctorHoliday, ObjectId> {
    Optional<DoctorHoliday> findByDoctorIdAndClinicId(String doctorId, String clinicId);
}
