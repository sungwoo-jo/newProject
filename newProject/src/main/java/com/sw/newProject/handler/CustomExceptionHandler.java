package com.sw.newProject.handler;

import com.sw.newProject.dto.ErrorDto;
import com.sw.newProject.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorDto> handleCustomException(CustomException ex) {
        // ErrorDto 생성 (HttpStatus는 여기서 처리)
        log.error("Error occurred: Code: {}, Message: {}, Detail: {}",
                ex.getErrorCode().getCode(),
                ex.getErrorCode().getMsg(),
                ex.getDetail());
        ErrorDto errorDto = ErrorDto.fromException(ex);
        return new ResponseEntity<>(errorDto, ex.getStatus());  // 상태 코드는 여기서 설정
    }
}
