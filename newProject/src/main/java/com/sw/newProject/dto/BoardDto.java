package com.sw.newProject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto extends BaseTimeEntity{

    /*
     * 게시판 기본 Dto
     * todo: 게시판 성격에 따라 추가할 필드는 추후 테이블 연관관계로 표현 필요
     */
    @NotNull
    private Integer boardNo; // 게시글번호

    @NotNull
    private Integer memNo; // 회원번호

    @NotNull
    private String writerNm; // 작성자명

    @NotNull
    private String subject; // 제목

    @NotNull
    private String contents; // 내용

    private String hashTag; // 해시태그
    private Boolean deleteYn; // 게시글 삭제여부
    private Integer likeCnt; // 좋아요 수
    private Integer hitCnt; // 조회수
    private Boolean hiddenFl; // 숨김여부
    private Boolean complaintFl; // 신고여부
    private Integer companionCnt; // 인원수
    private Integer foodCharge; // 식비
    private Integer transportCharge; // 차비
    private Integer etcCharge; // 부대비용
    private Integer budget; // 예산
    private String district; // 여행지 정보
    private LocalDateTime visitDt; // 방문일시
}
