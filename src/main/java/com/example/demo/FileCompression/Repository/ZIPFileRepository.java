package com.example.demo.FileCompression.Repository;

import com.example.demo.FileCompression.Entity.ZIPFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZIPFileRepository extends MongoRepository<ZIPFile, Integer> {
    // Additional query methods if needed

}