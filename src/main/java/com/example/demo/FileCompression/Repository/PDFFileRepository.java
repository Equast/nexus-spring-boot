package com.example.demo.FileCompression.Repository;

import com.example.demo.FileCompression.Entity.PDFFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PDFFileRepository extends MongoRepository<PDFFile, Integer> {
    // Additional query methods if needed
}
