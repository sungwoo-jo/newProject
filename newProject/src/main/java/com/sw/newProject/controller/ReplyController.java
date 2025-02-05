package com.sw.newProject.controller;

import com.sw.newProject.dto.ReplyDto;
import com.sw.newProject.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
        return replyDto;
    }

    @PostMapping("/{boardId}/doWrite/{boardNo}") // 댓글 작성
    public void doWrite(@PathVariable String boardId, @PathVariable Integer boardNo, @RequestBody String contents) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("boardId", boardId);
        map.put("boardNo", boardNo);
        map.put("contents", contents);
        replyService.doWrite(map);
    }

    @DeleteMapping("/{boardId}/doDelete/{replyNo}") // 댓글 삭제(softDelete)
    public void doDelete(@PathVariable String boardId, @PathVariable Integer replyNo) {
        log.debug("replyNo: " + replyNo);
        HashMap<String, Object> map = new HashMap<>();
        map.put("boardId", boardId);
        map.put("replyNo", replyNo);
        replyService.doDelete(map);
    }

    @PatchMapping("/{boardId}/doUpdate/{replyNo}") // 댓글 수정
    public void doUpdate(@PathVariable String boardId, @PathVariable Integer replyNo, @RequestBody String contents) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("boardId", boardId);
        map.put("replyNo", replyNo);
        map.put("contents", contents);
        replyService.doUpdate(map);
    }
}
