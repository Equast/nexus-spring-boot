package com.example.demo.SearchEngine.Repository;

import com.example.demo.SearchEngine.Entity.AllDoctorsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoRepo extends MongoRepository<AllDoctorsEntity, String> {
    // Custom query methods if needed
}
