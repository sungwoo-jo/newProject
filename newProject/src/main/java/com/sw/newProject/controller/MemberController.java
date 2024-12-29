package com.sw.newProject.controller;

import com.sw.newProject.dto.MemberDto;

import com.sw.newProject.service.MemberService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/join") // 회원가입 페이지
    public String getJoinPage() { // 회원가입 페이지
        return "join";
    }

    @PostMapping("/doJoin") // 회원가입 처리
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

    @GetMapping("/memberList") // 전체 회원 리스트 가져오기
    public String getAllMember(Model model) {
        List<MemberDto> members = memberService.getAllMember();
        model.addAttribute("members", members);
        return "memberList"; // memberList.html
    }

    @GetMapping("/update") // 정보수정 페이지
    public String getUpdatePage() {
        return "update";
    }

    @PatchMapping("/doUpdate") // 정보수정 처리
    public String updateMember(@RequestBody MemberDto memberDto) {
        memberService.updateMember(memberDto);
        return "joinSuccess";
    }

    @DeleteMapping("/doDelete") // 회원탈퇴 처리
    public String deleteMember(@RequestBody MemberDto memberDto) {
        memberService.deleteMember(memberDto.getMemNo());
        return "deleteSuccess";
    }
}

