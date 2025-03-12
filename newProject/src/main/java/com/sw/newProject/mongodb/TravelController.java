package com.sw.newProject.mongodb;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mongo/travel")
@RequiredArgsConstructor
public class TravelController {
    private final TravelService travelService;

    // 여행지 저장
    @PostMapping("/save")
    public TravelDocument save(@RequestBody TravelDocument travel) {
        return travelService.saveTravel(travel);
    }

    // 특정 여행지 검색
    @GetMapping("/search")
    public List<TravelDocument> search(@RequestParam String destination) {
        return travelService.searchByDestination(destination);
    }

    // 전체 여행지 조회
    @GetMapping("/all")
    public List<TravelDocument> getAll() {
        return travelService.getAllTravels();
    }
}
