package com.sw.newProject.dto.board;

import com.sw.newProject.dto.BaseTimeEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardIncrementDto extends BaseTimeEntity {

    /*
     * 게시글 조회 Dto
     * 회원은 회원번호와 조회일자를 기준으로 중복 여부를 판단하고, 비회원은 IP와 조회일자를 기준으로 중복 여부를 판단한다.
     */
    private Integer sno; // 번호

    private Integer boardNo; // 게시글 번호

    private String boardId; // 게시판 종류

    private Integer memNo; // 회원번호(비회원: 0)

    private String IP; // IP(비회원은 IP 기준으로 중복 조회를 제한)
}