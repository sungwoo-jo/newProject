package com.sw.newProject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * trace < debug < info < warn < error
 * default : info
 */
// 로그 테스트를 위해 추가한 컨트롤러 클래스
@RestController
@Slf4j
public class LogController {
    @GetMapping("/log")
    public void log(){
        log.trace("trace message");
        log.debug("debug message");
        log.info("info message"); // default
        log.warn("warn message");
        log.error("error message");
    }
}