package com.example.demo.SearchEngine.Repository;

import com.example.demo.SearchEngine.Entity.ElasticSearchEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticRepo extends ElasticsearchRepository<ElasticSearchEntity, String> {
    // No custom query methods added; inherits standard CRUD operations
}
