package com.sw.newProject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadFileDto extends BaseTimeEntity{

    private int sno; // 파일 순서
    private String uploadFileName; // 업로드 당시 파일명
    private String storedFileName; // 저장된 파일명(member 테이블에서 profileImageName와 동일한 이름을 가져야한다.)
}
