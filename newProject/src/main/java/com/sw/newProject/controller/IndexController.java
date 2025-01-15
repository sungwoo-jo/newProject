package com.sw.newProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/") // 메인 페이지 호출
    public String index() {
        return "/index";
    }
}
