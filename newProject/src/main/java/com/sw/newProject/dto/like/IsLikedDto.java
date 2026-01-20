package com.sw.newProject.dto.like;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IsLikedDto {
    private int memNo;
    private int boardNo;
    private String boardId;
    private String regDt;
}
