package com.sw.newProject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sw.newProject.dto.FriendShipDto;
import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.service.FriendShipService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> createRequest(@RequestBody FriendShipDto friendShipDto, HttpSession session) { // 친구 요청을 전송
        MemberDto memberDto = (MemberDto) session.getAttribute("member");
        friendShipDto.setToMemNo(memberDto.getMemNo());
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

    @PostMapping("/deleteFriend")
    public ResponseEntity<String> deleteFriend(FriendShipDto friendShipDto) { // 친구 삭제(서로의 목록에서 삭제, 요청 목록도 삭제)
        friendShipService.deleteFriend(friendShipDto);
        return ResponseEntity.ok("success");
    }
}
