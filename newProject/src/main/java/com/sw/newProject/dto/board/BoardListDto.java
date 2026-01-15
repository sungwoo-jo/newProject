package com.sw.newProject.dto.board;

import com.sw.newProject.dto.PageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardListDto {

    private BoardDto boardDto;
    private PageDto pageDto;
}
