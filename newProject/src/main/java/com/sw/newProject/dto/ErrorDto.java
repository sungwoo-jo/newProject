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

    public static ErrorDto fromException(CustomException ex) {
        ErrorCode errorType = ex.getErrorCode();
        return ErrorDto.builder()
                .code(errorType.getCode())
                .msg(errorType.getMsg())
                .detail(ex.getDetail())
                .build();
    }
}
