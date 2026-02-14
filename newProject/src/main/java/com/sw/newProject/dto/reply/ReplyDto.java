package com.sw.newProject.dto.reply;

import com.sw.newProject.dto.BaseTimeEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto extends BaseTimeEntity {
    private Integer replyNo; // 댓글번호
    private String boardId; // 게시판종류
    private Integer boardNo; // 게시글번호
    private Integer memNo; // 회원번호
    private String contents; // 내용
    private Integer groupNo; // 댓글 그룹 번호
    private Integer groupOrder; // 댓글 그룹 내 순서
    private Integer depth; // 뎁스(대댓글 표현)
    private Integer parentNo; // 부모댓글번호
    private Integer answerCnt; // 자식 댓글 갯수
    private Boolean deleteYn; // 삭제여부
    private Integer likeCnt; // 좋아요수
}
