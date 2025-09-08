package com.sw.newProject.mapper;

import com.sw.newProject.dto.BoardDto;
import com.sw.newProject.dto.GetBoardListDto;
import com.sw.newProject.dto.PageDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardDto> getBoardList(GetBoardListDto dto);

    int doWrite(BoardDto boardDto);

    BoardDto getBoardView(HashMap<String, Object> map);

    int doUpdate(HashMap<String, Object> map);

    int doDelete(HashMap<String, Object> map);

    List<BoardDto> doSearch(HashMap<String, Object> map);

    void incrementHitCnt(HashMap<String, Object> map);

    int doLike(HashMap<String, Object> map);

    List<BoardDto> getPopularBoard(String boardId);

    int getBoardCount(String boardId);

    int getBoardSearchCount(HashMap<String, Object> map);

    Integer getMemNoByBoardNo(HashMap<String, Object> map);

    Integer viewHitCnt(HashMap<String, Object> map);

    void insertHitInfo(HashMap<String, Object> map);
}
