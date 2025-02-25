package com.sw.newProject.exception;

import com.sw.newProject.enumType.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String detail;

    // HttpStatus를 동적으로 가져오는 메서드 추가
    public HttpStatus getStatus() {
        return errorCode.getStatus();  // ErrorCode에서 HttpStatus를 가져옴
    }
}
