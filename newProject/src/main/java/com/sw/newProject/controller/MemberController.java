package com.sw.newProject.controller;

import com.sw.newProject.dto.MemberDto;

import com.sw.newProject.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Controller
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/join") // 회원가입 페이지 호출
    public String getJoinPage() {
        return "join";
    }

    @PostMapping("/doJoin") // 회원가입 처리
    public ResponseEntity<String> insertMember(MemberDto memberDto) throws NoSuchAlgorithmException { // 회원가입 처리 후 boolean 으로 가입 여부 반환(0: 실패, 1: 성공)
        System.out.println("doJoin 호출");
        memberDto.setDeleteYn(false);
        memberDto.setMemPw(memberService.passwordEncrypt(memberDto.getMemPw()));

        memberService.insertMember(memberDto);
        return ResponseEntity.ok("success"); // todo: 회원가입 완료 페이지로 이동해야 함
    }

    @GetMapping("/memberList") // 전체 회원 리스트 가져오기
    public String getAllMember(Model model) {
        List<MemberDto> members = memberService.getAllMember();
        model.addAttribute("members", members);
        return "memberList"; // memberList.html
    }

    @GetMapping("/update") // 정보수정 페이지 호출
    public String getUpdatePage() {
        return "update";
    }

    @PatchMapping("/doUpdate") // 정보수정 처리
    public String updateMember(@RequestBody MemberDto memberDto) {
        memberService.updateMember(memberDto);
        return "joinSuccess"; // todo: 마이페이지 메인으로 이동해야 함
    }

    @DeleteMapping("/doDelete") // 회원탈퇴 처리
    public String deleteMember(@RequestBody MemberDto memberDto) {
        memberService.deleteMember(memberDto.getMemNo());
        return "deleteSuccess";
    }

    @GetMapping("/duplicationIdCheck") // 중복 ID 검증
    @ResponseBody
    public ResponseEntity<Long> duplicationIdCheck(@RequestParam String memId) {
        System.out.println("[memberController][duplicationIdCheck][memId]: " + memId);
        Long count = memberService.duplicationIdCheck(memId);

        return ResponseEntity.ok(count);
    }

    @GetMapping("/duplicationNickNmCheck") // 중복 닉네임 검증
    @ResponseBody
    public ResponseEntity<Long> duplicationNickNmCheck(@RequestParam String nickNm) {
        System.out.println("[memberController][duplicationNickNmCheck][nickNm]: " + nickNm);
        Long count = memberService.duplicationNickNmCheck(nickNm);

        return ResponseEntity.ok(count);
    }

    @GetMapping("/duplicationEmailCheck") // 중복 이메일 검증
    @ResponseBody
    public ResponseEntity<Long> duplicationEmailCheck(@RequestParam String email) {
        System.out.println("[memberController][duplicationEmailCheck][email]: " + email);
        Long count = memberService.duplicationEmailCheck(email);

        return ResponseEntity.ok(count);
    }

    @GetMapping("/login") // 로그인 페이지 호출
    public String getLoginPage() {
        return "login";
    }

    @PostMapping("/doLogin") // 로그인 처리
    public boolean doLogin(@RequestParam String memId, @RequestParam String memPw, HttpSession session) throws NoSuchAlgorithmException {
        MemberDto member = memberService.doLogin(memId, memPw);
        if (member == null) { // 회원 정보 존재하지 않을 때
            return false;
        }
        session.setAttribute("member", member);
        return true;
    }
}

