package com.sw.newProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.newProject.dto.DoResetPwDto;
import com.sw.newProject.dto.MemberDto;

import com.sw.newProject.service.MemberService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@OpenAPIDefinition(info = @Info(title = "newProject API 명세서",
        description = "API 명세서",
        version = "v1",
        contact = @Contact(name = "sungwoo-jo", email = "sungwoo9671@naver.com")
)
)

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
    public String insertMember(@RequestPart("memberDto") String memberDtoJson, @RequestPart("profileImage") MultipartFile profileImage) throws Exception { // 회원가입 처리 후 boolean 으로 가입 여부 반환(0: 실패, 1: 성공)

        // JSON 문자열을 MemberDto 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        MemberDto memberDto = objectMapper.readValue(memberDtoJson, MemberDto.class); // JSON -> 객체 변환

        System.out.println("doJoin 호출");
        System.out.println("memberDto: " + memberDto);
        System.out.println("doJoin memPw: " + memberDto.getMemPw());
        memberDto.setDeleteYn(false);
        memberDto.setMemPw(memberService.passwordEncrypt(memberDto.getMemPw()));

        memberService.insertMember(memberDto, profileImage);
        return "redirect:/joinSuccess"; // todo: 회원가입 완료 페이지로 이동해야 함
    }

    @GetMapping("joinSuccess")
    public String getJoinSuccessPage() {
        return "joinSuccess";
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
    public ResponseEntity<Boolean> doLogin(@RequestParam String memId, @RequestParam String memPw, HttpSession session) throws NoSuchAlgorithmException {
        MemberDto member = memberService.doLogin(memId, memPw);
        if (member == null) { // 회원 정보 존재하지 않을 때
            return ResponseEntity.ok(false);
        }
        session.setAttribute("member", member);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/findId") // 아이디 찾기 페이지 호출
    public String findId() {
        return "findId";
    }

    @PostMapping("/doFindId") // 아이디 찾기 처리
    public ResponseEntity<String> doFindId(@RequestParam String memNm, @RequestParam String email) throws MessagingException, ExecutionException, InterruptedException {
        Future<String> result = memberService.doFindId(memNm, email);
        return ResponseEntity.ok("sendMailSuccess");
    }

    @GetMapping("/findPw") // 비밀번호 찾기 페이지 호출
    public String findPw() {
        return "findPw";
    }

    @PostMapping("doFindPw") // 비밀번호 찾기 처리(회원 정보 일치 여부 확인)
    public String doFindPw(@RequestBody String memNm, @RequestBody String email, @RequestBody String memId, Model model) {
        System.out.println("[MemberController][doFindPw][memNm]: " + memNm);
        System.out.println("[MemberController][doFindPw][email]: " + email);
        System.out.println("[MemberController][doFindPw][memId]: " + memId);
        Integer memNo = memberService.doFindPw(memNm, email, memId);
        System.out.println(memNo);
//        if (memNo > 0) {
//            model.addAttribute("memNo", memNo);
//            return "/resetPw";
//        } else {
//            return "/findPw";
//        }
        return "hello";
    }

    @GetMapping("resetPw") // 새로운 비밀번호 설정 페이지
    public String resetPw(Model model) {
        return "resetPw";
    }

    @PatchMapping("doResetPw") // 비밀번호 재설정 처리
    public ResponseEntity<String> doResetPw(@RequestBody DoResetPwDto doResetPwDto) throws NoSuchAlgorithmException {
        System.out.println("[MemberController][doResetPw][doResetPwDto]: " + doResetPwDto);
        memberService.doResetPw(doResetPwDto); // 회원 번호, 새롭게 설정할 비밀번호
        return ResponseEntity.ok("resetPwSuccess");
    }

    @GetMapping("resetPwSuccess") // 비밀번호 재설정 완료 페이지 호출
    public String resetPwSuccess() {
        return "resetPwSuccess";
    }
}

