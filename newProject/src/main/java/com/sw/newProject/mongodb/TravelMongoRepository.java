package com.sw.newProject.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TravelMongoRepository extends MongoRepository<TravelDocument, String> {
    List<TravelDocument> findByDestination(String destination);
}