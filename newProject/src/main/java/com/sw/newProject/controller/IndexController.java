package com.sw.newProject.controller;

import com.sw.newProject.dto.BoardDto;
import com.sw.newProject.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final BoardService boardService;

    @GetMapping("/") // 메인 페이지 호출
    public String index(Model model) {
        List<BoardDto> boardDto = boardService.getPopularBoard("travel");
        model.addAttribute("boardDto", boardDto);
        return "index";
    }
}
