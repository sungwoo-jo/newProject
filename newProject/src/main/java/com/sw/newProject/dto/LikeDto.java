package com.sw.newProject.dto;

import com.sw.newProject.dto.board.BoardDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeDto {

    private BoardDto boardDto;

    private int likerMemNo; // 좋아요 누른 회원 번호
}
