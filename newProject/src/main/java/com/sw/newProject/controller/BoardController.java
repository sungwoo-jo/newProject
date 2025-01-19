package com.sw.newProject.controller;

import com.sw.newProject.dto.BoardDto;
import com.sw.newProject.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
}
