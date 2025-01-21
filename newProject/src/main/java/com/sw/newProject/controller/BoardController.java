package com.sw.newProject.controller;

import com.sw.newProject.dto.BoardDto;
import com.sw.newProject.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("{boardId}/list") // 게시글 리스트 페이지 호출
    public String getBoardList(@PathVariable String boardId, Model model) {
        List<BoardDto> boardDto = boardService.getBoardList(boardId);
        model.addAttribute("boards", boardDto);
        return "board/list";
    }

    @GetMapping("{boardId}/write") // 게시글 작성 페이지 호출
    public String getWritePage(@PathVariable String boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "board/write";
    }

    @GetMapping("{boardId}/view/{boardNo}") // 게시글 상세보기 페이지 호출
    public String getViewPage(@PathVariable String boardId, @PathVariable Integer boardNo, Model model) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("boardId", boardId);
        map.put("boardNo", boardNo);

        BoardDto boardDto = boardService.getBoardView(map);
        model.addAttribute("boardDto", boardDto);
        model.addAttribute("boardId", boardId);
        log.debug("boardDto: " + boardDto);
        return "board/view";
    }

    @PostMapping("{boardId}/doWrite") // 게시글 작성 처리
    public ResponseEntity<String> doWrite(@RequestBody BoardDto boardDto, @PathVariable String boardId) {
        boardService.doWrite(boardDto);
        return ResponseEntity.ok("success");
    }

    @PatchMapping("/doUpdate") // 게시글 수정 처리
    public ResponseEntity<String> doUpdate(@RequestBody BoardDto boardDto) {
        // todo
        return ResponseEntity.ok("success");
    }

    @PostMapping("/doDelete") // 게시글 삭제 처리
    public ResponseEntity<String> doDelete(@RequestBody BoardDto boardDto) {
        // todo
        return ResponseEntity.ok("success");
    }


}
