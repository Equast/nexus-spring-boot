package com.example.demo.SearchEngine.Service;

import com.example.demo.SearchEngine.Entity.AllDoctorsEntity;
import com.example.demo.SearchEngine.Entity.ElasticSearchEntity;

import com.example.demo.SearchEngine.Repository.ElasticRepo;
import com.example.demo.SearchEngine.Repository.MongoRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class SyncService {

    @Autowired
    private MongoRepo mongoRepo;

    @Autowired
    private ElasticRepo elasticSearchRepository;

    // Synchronize data at startup
    @PostConstruct
    public void syncData() {
        List<AllDoctorsEntity> mongoData = mongoRepo.findAll();
        for (AllDoctorsEntity doctor : mongoData) {
            ElasticSearchEntity elasticSearchEntity = new ElasticSearchEntity();
            elasticSearchEntity.setId(doctor.getId());
            elasticSearchEntity.setName(doctor.getName());
            elasticSearchEntity.setSpecialization(doctor.getSpecialization());
            elasticSearchEntity.setQualification(doctor.getQualification());
            elasticSearchEntity.setExperience(doctor.getExperience());
            elasticSearchEntity.setClinics(doctor.getClinics());

            elasticSearchEntity.setJoiningDate(doctor.getJoiningDate());
            elasticSearchEntity.setProfileImage(doctor.getProfileImage());
            elasticSearchEntity.setAbout(doctor.getAbout());

            // Check if the record already exists in Elasticsearch
            if (elasticSearchRepository.existsById(elasticSearchEntity.getId())) {
                // Update existing record
                elasticSearchRepository.save(elasticSearchEntity);
            } else {
                // Save new record
                elasticSearchRepository.save(elasticSearchEntity);
            }
        }
    }
}
