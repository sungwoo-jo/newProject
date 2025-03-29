package com.sw.newProject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.service.FriendShipService;
import com.sw.newProject.service.MemberService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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
    private final FriendShipService friendShipService;
    private final ObjectMapper objectMapper;

    @GetMapping(value = {"/", "/index", ""})
    public String getIndexPage(HttpSession session) throws JsonProcessingException {
        MemberDto memberDto = (MemberDto) session.getAttribute("member");
        String friendList = friendShipService.getFriendList(memberDto.getMemNo());

        List<MemberDto> friends = new ArrayList<>();

        // JSON 문자열을 JSONObject로 파싱
        JSONObject friendListJson = new JSONObject(friendList);

        // 친구 목록 순회
        for (String friendId : friendListJson.keySet()) {
            String timestamp = friendListJson.getString(friendId);
            System.out.println("Friend ID: " + friendId + ", Time: " + timestamp);

            MemberDto friendMemberDto = memberService.getMember(Integer.parseInt(friendId));

            friends.add(friendMemberDto);
        }

        log.info("friends: {}", friends);

        session.setAttribute("friends", friends);

        return "/mypage/index";
    }

    @GetMapping("/update")
    public String update() { // 정보 수정 페이지 호출
        return "/mypage/update";
    }

    @PostMapping("/doUpdate")
    public String doUpdate(MemberDto memberDto, HttpSession session) throws NoSuchAlgorithmException {
        log.info("memberDto: {}", memberDto);
        memberService.updateMember(memberDto);
        MemberDto updatedMemberDto = memberService.getMember(memberDto.getMemNo());
        session.setAttribute("member", updatedMemberDto);
        return "/mypage/index";
    }

    @GetMapping("/follow/list")
    public String getFollowList(Model model) {
        
        return "/mypage/followList";
    }

    @GetMapping("/following/list")
    public String getFollowingList(Model model) {
        return "/mypage/followingList";
    }
}
