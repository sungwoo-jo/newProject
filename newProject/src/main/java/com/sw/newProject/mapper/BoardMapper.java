package com.sw.newProject.mapper;

import com.sw.newProject.dto.board.BoardDto;
import com.sw.newProject.dto.board.BoardListDto;
import com.sw.newProject.dto.board.BoardSearchDto;
import com.sw.newProject.dto.like.IsLikedDto;
import com.sw.newProject.dto.like.LikeDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardDto> getBoardList(BoardListDto boardListDto);

    int doWrite(BoardDto boardDto);

    BoardDto getBoardView(BoardDto boardDto);

    int doUpdate(BoardDto boardDto);

    int doDelete(BoardDto boardDto);

    List<BoardDto> doSearch(BoardSearchDto boardSearchDto);

    void incrementHitCnt(BoardDto boardDto);

    int doLike(LikeDto likeDto);

    List<BoardDto> getPopularBoard(String boardId);

    int getBoardCount(String boardId);

    int getBoardSearchCount(BoardSearchDto boardSearchDto);

    Integer viewHitCnt(BoardDto boardDto);

    void insertHitInfo(BoardDto boardDto);

    int getWriterMemNo(BoardDto boardDto);

    int isLiked(IsLikedDto dto);

    void saveLikeData(IsLikedDto isLikedDto);
}
