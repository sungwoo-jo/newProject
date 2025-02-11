package com.sw.newProject.mapper;

import com.sw.newProject.dto.BoardDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardDto> getBoardList(String boardId);

    int doWrite(BoardDto boardDto);

    BoardDto getBoardView(HashMap<String, Object> map);

    int doUpdate(HashMap<String, Object> map);

    int doDelete(HashMap<String, Object> map);

    List<BoardDto> doSearch(HashMap<String, Object> map);

    void incrementHitCnt(HashMap<String, Object> map);

    int doLike(HashMap<String, Object> map);
}
