package com.sw.newProject.controller;

import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.service.MemberService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@OpenAPIDefinition(info = @Info(title = "newProject API 명세서",
        description = "API 명세서",
        version = "v1",
        contact = @Contact(name = "sungwoo-jo", email = "sungwoo9671@naver.com")
)
)

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    private final MemberService memberService;

    @GetMapping(value = {"/", "/index", ""})
    public String getIndexPage() {
        return "/mypage/index";
    }

    @GetMapping("/update")
    public String update() { // 정보 수정 페이지 호출
        return "/mypage/update";
    }

    @PostMapping("/doUpdate")
    public String doUpdate(MemberDto memberDto, HttpSession session) {
        log.info("memberDto: {}", memberDto);
        memberService.updateMember(memberDto);
        MemberDto updatedMemberDto = memberService.getMember(memberDto.getMemNo());
        session.setAttribute("member", updatedMemberDto);
        return "/mypage/index";
    }
}
