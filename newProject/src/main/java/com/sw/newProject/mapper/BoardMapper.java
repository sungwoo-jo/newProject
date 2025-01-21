package com.sw.newProject.mapper;

import com.sw.newProject.dto.BoardDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardDto> getBoardList(String boardId);

    void doWrite(BoardDto boardDto);

    BoardDto getBoardView(HashMap<String, Object> map);
}
