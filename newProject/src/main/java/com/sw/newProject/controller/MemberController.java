package com.sw.newProject.controller;

import com.sw.newProject.dto.DoResetPwDto;
import com.sw.newProject.dto.MemberDto;

import com.sw.newProject.service.MemberService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/join") // 회원가입 페이지 호출
    public String getJoinPage() {
        return "/member/join";
    }

    @PostMapping("/doJoin") // 회원가입 처리
    public String insertMember(MemberDto memberDto) throws Exception { // 회원가입 처리 후 boolean 으로 가입 여부 반환(0: 실패, 1: 성공)

        // JSON 문자열을 MemberDto 객체로 변환
//        ObjectMapper objectMapper = new ObjectMapper();
//        MemberDto memberDto = objectMapper.readValue(memberDtoJson, MemberDto.class); // JSON -> 객체 변환

        System.out.println("doJoin 호출");
        System.out.println("memberDto: " + memberDto);
        System.out.println("doJoin memPw: " + memberDto.getMemPw());
        memberDto.setDeleteYn(false);
        memberDto.setMemPw(memberService.passwordEncrypt(memberDto.getMemPw()));

        memberService.insertMember(memberDto);
        return "/member/joinSuccess"; // todo: 회원가입 완료 페이지로 이동해야 함
    }

    @GetMapping("/joinSuccess")
    public String getJoinSuccessPage() {
        return "/member/joinSuccess";
    }

    @GetMapping("/memberList") // 전체 회원 리스트 가져오기
    public String getAllMember(Model model) {
        List<MemberDto> members = memberService.getAllMember();
        model.addAttribute("members", members);
        return "/member/memberList"; // memberList.html
    }

    @PatchMapping("/doUpdate") // 정보수정 처리
    public String updateMember(@RequestBody MemberDto memberDto) {
        memberService.updateMember(memberDto);
        return "/member/joinSuccess"; // todo: 마이페이지 메인으로 이동해야 함
    }

    @GetMapping("/delete") // 회원 탈퇴 페이지 호출
    public String getDeletePage() {
        return "/member/delete";
    }

    @PostMapping("/doDelete") // 회원탈퇴 처리
    @ResponseBody
    public String deleteMember(@RequestBody MemberDto memberDto, HttpSession session) {
        Integer memNo = memberDto.getMemNo();
        System.out.println("memNo: " + memNo);

        try {
            if (memNo != null && memNo != 0) {
                memberService.deleteMember(memNo);
                session.removeAttribute("member");
                return "Success";
            }
            return "Fail";
        } catch (Exception e) {
            return "Fail";
        }
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
        return "/member/login";
    }

    @PostMapping("/doLogin") // 로그인 처리
    public ResponseEntity<String> doLogin(@RequestBody MemberDto memberDto, HttpSession session) throws NoSuchAlgorithmException {
        MemberDto member = memberService.doLogin(memberDto);
        if (member != null && member.getDeleteYn() != Boolean.TRUE) { // 로그인 성공
            // 세션 값 설정
            session.setAttribute("member", member);
            return ResponseEntity.ok("success");
        } else { // 로그인 실패
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/logout") // 로그아웃 처리
    public String doLogout(HttpSession session) {
        session.removeAttribute("member");
        log.info("로그아웃 진행 -> 세션 삭제 완료");
        return "redirect:/";
    }

    @GetMapping("/findId") // 아이디 찾기 페이지 호출
    public String findId() {
        return "/member/findId";
    }

    @PostMapping("/doFindId") // 아이디 찾기 처리
    public ResponseEntity<String> doFindId(@RequestBody MemberDto memberDto) throws MessagingException, ExecutionException, InterruptedException, NotFoundException {
        if (memberService.findId(memberDto.getMemNm(), memberDto.getEmail()) == null) {
            log.info("member fot nound");
            throw new NotFoundException("입력하신 회원 정보를 다시 한 번 확인해주세요.");
        }
        Future<String> result = memberService.sendMailFindId(memberDto);
        return ResponseEntity.ok("sendMailSuccess");
    }

    @GetMapping("/findPw") // 비밀번호 찾기 페이지 호출
    public String findPw() {
        return "/member/findPw";
    }

    @PostMapping("doFindPw") // 비밀번호 찾기 처리(회원 정보 일치 여부 확인)
    public ResponseEntity<String> doFindPw(@RequestBody MemberDto memberDto) throws NoSuchAlgorithmException, NotFoundException {
        String memNm = memberDto.getMemNm();
        String email = memberDto.getEmail();
        String memId = memberDto.getMemId();
        if (memberService.findPw(memberDto.getMemNm(), memberDto.getEmail(), memberDto.getMemId()) == null) {
            log.info("member fot nound");
            throw new NotFoundException("입력하신 회원 정보를 다시 한 번 확인해주세요.");
        }
        Future<String> result = memberService.sendMailFindPw(memberDto);
        return ResponseEntity.ok("sendMailSuccess");
    }

    @GetMapping("resetPw") // 임시 비밀번호 발송 완료 페이지 호출
    public String resetPw() {
        return "/member/resetPw";
    }

    @PatchMapping("doResetPw") // 비밀번호 재설정 처리
    public ResponseEntity<String> doResetPw(@RequestBody DoResetPwDto doResetPwDto) throws NoSuchAlgorithmException {
        System.out.println("[MemberController][doResetPw][doResetPwDto]: " + doResetPwDto);
        memberService.doResetPw(doResetPwDto); // 회원 번호, 새롭게 설정할 비밀번호
        return ResponseEntity.ok("resetPwSuccess");
    }

    @GetMapping("resetPwSuccess") // 비밀번호 재설정 완료 페이지 호출
    public String resetPwSuccess() {
        return "/member/resetPwSuccess";
    }
}

