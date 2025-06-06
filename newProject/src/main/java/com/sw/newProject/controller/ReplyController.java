package com.sw.newProject.controller;

import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.dto.ReplyDto;
import com.sw.newProject.service.ReplyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/{boardId}/getReply/{boardNo}") // 댓글 조회
    public List<ReplyDto> getReply(@PathVariable String boardId, @PathVariable Integer boardNo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("boardId", boardId);
        map.put("boardNo", boardNo);
        List<ReplyDto> replyDto = replyService.getReply(map);
        log.info("{}", replyDto);
        return replyDto;
    }

    @PostMapping("/{boardId}/doWrite/{boardNo}") // 댓글 작성
    public void doWrite(@PathVariable String boardId, @PathVariable Integer boardNo, @RequestBody ReplyDto replyDto, HttpSession session) {
        MemberDto memberDto = (MemberDto) session.getAttribute("member");
        if (replyDto.getParentNo() == null) {
            replyDto.setDepth(0); // 일반 댓글
        } else {
            replyDto.setDepth(1); // 대댓글
        }
        replyDto.setBoardId(boardId);
        replyDto.setBoardNo(boardNo);
        replyDto.setMemNo(memberDto.getMemNo());

        replyService.doWrite(replyDto);
    }

    @DeleteMapping("/{boardId}/doDelete/{replyNo}") // 댓글 삭제(softDelete)
    public void doDelete(@PathVariable String boardId, @PathVariable Integer replyNo) {
        log.debug("replyNo: " + replyNo);
        HashMap<String, Object> map = new HashMap<>();
        map.put("boardId", boardId);
        map.put("replyNo", replyNo);
        log.info("{}", map);
        replyService.doDelete(map);
    }

    @PatchMapping("/{boardId}/doUpdate/{replyNo}") // 댓글 수정
    public void doUpdate(@PathVariable String boardId, @PathVariable Integer replyNo, @RequestBody String contents) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("boardId", boardId);
        map.put("replyNo", replyNo);
        map.put("contents", contents);
        log.info("{}", map);
        replyService.doUpdate(map);
    }
}
