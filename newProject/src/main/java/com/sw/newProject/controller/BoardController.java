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

import static java.lang.Boolean.TRUE;

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

    @GetMapping(value = {"{boardId}/write/{boardNo}", "{boardId}/write", "{boardId}/write/"}) // 게시글 작성 페이지 호출
    public String getWritePage(@PathVariable String boardId, @PathVariable(value = "boardNo", required = false) Integer boardNo, Model model) {
        log.debug("boardNo: " + boardNo);
        if (boardNo != null) {
            HashMap<String, Object> map = new HashMap<>();

            map.put("boardId", boardId);
            map.put("boardNo", boardNo);

            BoardDto boardDto = boardService.getBoardView(map);
            model.addAttribute("boardDto", boardDto);
            return "board/write";
        }
        model.addAttribute("boardId", boardId);
        return "board/write";
    }

    @GetMapping("{boardId}/view/{boardNo}") // 게시글 상세보기 페이지 호출
    public String getViewPage(@PathVariable String boardId, @PathVariable Integer boardNo, Model model) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("boardId", boardId);
        map.put("boardNo", boardNo);

        BoardDto boardDto = boardService.getBoardView(map);

        if (boardDto.getDeleteYn() != TRUE) {
            model.addAttribute("boardDto", boardDto);
            model.addAttribute("boardId", boardId);
            log.debug("boardDto: " + boardDto);
            return "board/view";
        } else { // 게시글이 삭제된 상태
            model.addAttribute("boardId", boardId);
            model.addAttribute("notFound", "삭제된 게시글입니다.");
            return "redirect:../list";
        }
    }

    @PostMapping("{boardId}/doWrite") // 게시글 작성 처리
    public ResponseEntity<String> doWrite(@RequestBody BoardDto boardDto, @PathVariable String boardId) {
        int result = 0;
        if (boardDto.getBoardNo() != null) { // 수정
            HashMap<String, Object> map = new HashMap<>();
            map.put("boardId", boardId);
            map.put("boardDto", boardDto);

            result = boardService.doUpdate(map);
        } else {
            result = boardService.doWrite(boardDto);
        }
        return result > 0 ? ResponseEntity.ok("success") : ResponseEntity.ok("fail");
    }

    @PostMapping("{boardId}/doDelete") // 게시글 삭제 처리
    public ResponseEntity<String> doDelete(@RequestBody BoardDto boardDto, @PathVariable String boardId) {
        int result = 0;
        if (boardDto.getBoardNo() != null && boardId != null) { // 삭제 진행
            HashMap<String, Object> map = new HashMap<>();
            map.put("boardDto", boardDto);
            map.put("boardId", boardId);

            result = boardService.doDelete(map);
        }
        return result > 0 ? ResponseEntity.ok("success") : ResponseEntity.ok("fail");
    }

    @GetMapping("/{boardId}/doSearch") // 게시글 검색
    public String doSearch(@PathVariable String boardId, Model model, @RequestParam String type, @RequestParam String keyword) {
        if (!type.equals("") && !keyword.equals("")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("boardId", boardId);
            map.put("type", type);
            map.put("keyword", keyword);
            List<BoardDto> boardDto = boardService.doSearch(map);
            model.addAttribute("boards", boardDto);
        }
        // todo: 검색어가 없을 경우 알럿창 띄우기
        return "board/list";
    }
}
