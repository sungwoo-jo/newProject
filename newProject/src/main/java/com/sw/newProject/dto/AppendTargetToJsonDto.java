package com.sw.newProject.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AppendTargetToJsonDto {
    private int ownerMemNo; // JSON 데이터 소유자
    private int targetMemNo; // 추가할 회원 번호
    private String columnName; // DB 컬럼명
}
