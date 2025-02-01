package com.sw.newProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {
    private Integer replyNo; // 댓글번호
    private String boardId; // 게시판종류
    private Integer boardNo; // 게시글번호
    private Integer memNo; // 회원번호
    private String contents; // 내용
    private Integer depth; // 뎁스(대댓글 표현)
    private Integer seqNo; // 댓글순서
    private Integer parentNo; // 부모댓글번호
    private Boolean deleteYn; // 삭제여부
    private Integer likeCnt; // 좋아요수
    private LocalDateTime regDt; // 생성일시
    private LocalDateTime modDt; // 수정일시
}
