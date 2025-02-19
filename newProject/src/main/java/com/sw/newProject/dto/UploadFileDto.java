package com.sw.newProject.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UploadFileDto {

    private int sno; // 파일 순서
    private String uploadFileName; // 업로드 당시 파일명
    private String storedFileName; // 저장된 파일명(member 테이블에서 profileImageName와 동일한 이름을 가져야한다.)
    private LocalDateTime regDt;
    private LocalDateTime modDt;
}
