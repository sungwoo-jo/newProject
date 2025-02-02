package com.sw.newProject.controller;

import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.dto.PostDto;
import com.sw.newProject.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/list") // 쪽지 리스트 조회
    public String getPostList(HttpSession httpSession, Model model) {
        MemberDto member = (MemberDto) httpSession.getAttribute("member");
        List<PostDto> postDto = postService.getPostList(member.getMemNo());
        model.addAttribute("postDto", postDto);
        return "/post/list";
    }

    @GetMapping("/view/{postNo}") // 쪽지 상세보기 페이지 호출
    public String viewPost(@PathVariable Integer postNo, Model model, HttpSession httpSession) {
        PostDto postDto = postService.viewPost(postNo);
        model.addAttribute("postDto", postDto);
        return "/post/view";
    }

    @DeleteMapping("/doDelete/{postNo}") // 쪽지 삭제 처리
    public ResponseEntity<String> doDelete(@PathVariable Integer postNo) {
        Integer result = postService.doDelete(postNo);
        return result > 0 ? ResponseEntity.ok("success") : ResponseEntity.ok("fail");
    }

    @GetMapping(value = {"/write", "/write/"}) // 쪽지 작성 페이지 호출
    public String getWritePage(@RequestParam(required = false) String senderMemId, Model model) {
        model.addAttribute("senderMemId", senderMemId);
        return "/post/write";
    }

    @PostMapping("/doWrite") // 쪽지 전송 처리
    public ResponseEntity<String> doWrite(@RequestBody PostDto postDto, HttpSession httpSession) {
        MemberDto member = (MemberDto) httpSession.getAttribute("member");
        // 보내는사람 세팅
        postDto.setSenderMemId(member.getMemId());
        postDto.setSenderMemNo(member.getMemNo());
        // 받는사람 세팅
        postDto.setReceiverMemNo(postService.getMemNoOfMemId(postDto.getReceiverMemId()));
        postDto.setReceiverMemId(postDto.getReceiverMemId());
        // 여기부분 필수값들 뭔지 확인하고 아래 값들은 DEFAULT FALSE 로 설정하면 괜찮을듯
        // 그리고 sendDt, regDt는 INSERT 시 now() 함수로 현재시간으로 인서트시키기
        log.debug("postDto: " + postDto);
        Integer result = postService.doWrite(postDto);
        return result > 0 ? ResponseEntity.ok("success") : ResponseEntity.ok("fail");
    }
}
