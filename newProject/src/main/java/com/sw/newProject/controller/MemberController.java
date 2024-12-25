package com.sw.newProject.controller;

import com.sw.newProject.dto.MemberDto;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    @GetMapping("/join")
    public String getJoinPage() { // 회원가입 페이지
        return "join";
    }

    @PostMapping("/doJoin")
    public boolean doJoin(MemberDto memberDto) { // 회원가입 처리 후 boolean 으로 가입 여부 반환(0: 실패, 1: 성공)

    }
}
