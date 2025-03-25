package com.sw.newProject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sw.newProject.dto.FriendShipDto;
import com.sw.newProject.service.FriendShipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendShipController {

    private final FriendShipService friendShipService;

    @GetMapping("/getSentRequest/{memNo}")
    public ResponseEntity<List<FriendShipDto>> getSentRequest(@PathVariable int memNo) { // 보낸 모든 요청 조회
        log.info("memNo: {}", memNo);
        List<FriendShipDto> sentRequest = friendShipService.getSentRequest(memNo);
        return ResponseEntity.ok(sentRequest);
    }

    @PostMapping("/createRequest")
    public ResponseEntity<String> createRequest(FriendShipDto friendShipDto) { // 친구 요청을 전송
        log.info("friendShipDto: {}", friendShipDto);
        friendShipService.createRequest(friendShipDto);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/getReceivedRequest/{memNo}")
    public ResponseEntity<List<FriendShipDto>> getReceivedRequest(@PathVariable int memNo) { // 받은 모든 요청 조회
        List<FriendShipDto> receivedRequest = friendShipService.getReceivedRequest(memNo);
        return ResponseEntity.ok(receivedRequest);
    }

    @PostMapping("/acceptRequest")
    public ResponseEntity<String> acceptRequest(FriendShipDto friendShipDto) throws JsonProcessingException { // 친구 요청 수락
        friendShipService.acceptRequest(friendShipDto);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/rejectRequest")
    public ResponseEntity<String> rejectRequest(FriendShipDto friendShipDto) { // 친구 요청 거절
        friendShipService.rejectRequest(friendShipDto);
        return ResponseEntity.ok("success");
    }
}
