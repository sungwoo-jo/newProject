package com.sw.newProject.mongodb;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelService {
    private final TravelMongoRepository mongoRepository;

    // MongoDB에 여행지 저장
    public TravelDocument saveTravel(TravelDocument travel) {
        return mongoRepository.save(travel);
    }

    // 특정 여행지를 MongoDB에서 검색
    public List<TravelDocument> searchByDestination(String destination) {
        return mongoRepository.findByDestination(destination);
    }

    // 모든 여행지를 MongoDB에서 조회
    public List<TravelDocument> getAllTravels() {
        return mongoRepository.findAll();
    }
}
