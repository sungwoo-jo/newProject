package com.sw.newProject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    /*
     * 게시판 기본 Dto
     * todo: 게시판 성격에 따라 추가할 필드는 추후 테이블 연관관계로 표현 필요
     */
    @NotNull
    private Integer boardNo;

    @NotNull
    private Integer memNo;

    @NotNull
    private String writerNm;

    @NotNull
    private String subject;

    @NotNull
    private String contents;

    private String hashTag;
    private Boolean deleteYn;
    private Integer likeCnt;
    private Integer hitCnt;
    private Boolean hiddenFl;
    private Boolean complaintFl;
    private Integer companionCnt; // 인원수
    private Integer foodCharge; // 식비
    private Integer transportCharge; // 차비
    private Integer etcCharge; // 부대비용
    private Integer budget; // 예산
    private String district; // 여행지 정보
    private LocalDateTime visitDt; // 방문일시
    private LocalDateTime regDt; // 생성일시
    private LocalDateTime modDt; // 수정일시
}
