package com.sw.newProject.controller;

import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.dto.NotificationDto;
import com.sw.newProject.dto.PostDto;
import com.sw.newProject.enumType.NotificationType;
import com.sw.newProject.service.NotificationService;
import com.sw.newProject.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final NotificationService notificationService;

    /*
     * todo: 보낸 편지함, 받은 편지함으로 구분하기
     */
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

    /*
     * todo: 받은편지함 삭제 & 보낸편지함 삭제로 구분 필요
     */
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
        NotificationDto notificationDto = new NotificationDto();
        // 보내는사람 세팅
        postDto.setSenderMemId(member.getMemId());
        postDto.setSenderMemNo(member.getMemNo());
        // 받는사람 세팅
        postDto.setReceiverMemNo(postService.getMemNoOfMemId(postDto.getReceiverMemId()));
        postDto.setReceiverMemId(postDto.getReceiverMemId());
        Integer result = postService.doWrite(postDto);

        // 받는 사람에게 알림 전송
        notificationDto.setToMemNo(postDto.getReceiverMemNo());
        notificationDto.setFromMemNo(postDto.getSenderMemNo());
        notificationDto.setContent(postDto.getSenderMemId() + "님이 쪽지를 보냈습니다.");
        notificationDto.setUrl("/post/list");
        notificationDto.setNotificationType(NotificationType.POST_SEND);
        notificationService.notifyOne(notificationDto.getToMemNo(), notificationDto.getContent(), notificationDto.getNotificationType());

        return result > 0 ? ResponseEntity.ok("success") : ResponseEntity.ok("fail");
    }
}
