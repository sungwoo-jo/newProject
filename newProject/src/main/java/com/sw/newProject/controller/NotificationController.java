package com.sw.newProject.controller;

import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final HttpSession session;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        MemberDto memberDto = (MemberDto)session.getAttribute("member");
        Integer memNo = memberDto.getMemNo();
        return notificationService.subscribe(memNo);
    }

    @PostMapping("/send-data/{memNo}")
    public void sendData(@PathVariable Integer memNo) {
        notificationService.notifyOne(memNo, "data");
    }
}