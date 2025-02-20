package com.sw.newProject.dto;

import com.sw.newProject.enumType.ErrorCode;
import com.sw.newProject.exception.CustomException;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorDto {
    private String code;
    private String msg;
    private String detail;

    public static ResponseEntity<ErrorDto> toResponseEntity(CustomException ex) {
        ErrorCode errorType = ex.getErrorCode();
        String detail = ex.getDetail();

        return ResponseEntity
                .status(ex.getStatus())
                .body(ErrorDto.builder()
                        .code(errorType.getCode())
                        .msg(errorType.getMsg())
                        .detail(detail)
                        .build());
    }
}
