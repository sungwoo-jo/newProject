package com.sw.newProject.dto.board;

import com.sw.newProject.dto.PageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSearchDto {
    private BoardDto boardDto = new BoardDto();
    private String type;
    private String keyword;
    private PageDto pageDto;
}
