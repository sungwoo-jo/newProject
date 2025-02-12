package com.sw.newProject.controller;

import com.sw.newProject.enumType.ErrorCode;
import com.sw.newProject.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/develop/")
public class TestController {
    @GetMapping("v1/bad-request")
    public ResponseEntity<Object> test400Error() {
        try{
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_PASSWORD, "비밀번호를 확인해주세요.");
        }
        catch(Exception ex) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_PASSWORD, ex.getMessage());
        }
    }

    @GetMapping("v1/unauthorized")
    public ResponseEntity<Object> test401Error() {
        try{
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorCode.EXPIRED_TOKEN, "토큰이 만료되었습니다.");
        }
        catch(Exception ex) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorCode.EXPIRED_TOKEN, ex.getMessage());
        }
    }
}
