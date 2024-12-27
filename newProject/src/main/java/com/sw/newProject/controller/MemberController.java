package com.sw.newProject.controller;

import com.sw.newProject.dto.MemberDto;

import com.sw.newProject.service.MemberService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class MemberController {
    private MemberService memberService;
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/join")
    public String getJoinPage() { // 회원가입 페이지
        return "join";
    }

    @PostMapping("/doJoin")
    public ResponseEntity<String> insertMember(@RequestBody MemberDto memberDto) { // 회원가입 처리 후 boolean 으로 가입 여부 반환(0: 실패, 1: 성공)
        if (memberDto.getDeleteYn() == null) {
            memberDto.setDeleteYn(false);
        }

        System.out.println(memberDto);

        memberService.insertMember(memberDto);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("success");
    }
}

