package com.sw.newProject.service;

import com.sw.newProject.dto.BoardDto;
import com.sw.newProject.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;

    public List<BoardDto> getBoardList(String boardId) {
        return boardMapper.getBoardList(boardId);
    }

    public int doWrite(BoardDto boardDto) {
        if (boardDto.getMemNo() != null && !boardDto.getMemNo().equals("")) {
            boardDto.setMemNo(boardDto.getMemNo());
        } else {
            boardDto.setMemNo(0);
        }
        if (boardDto.getWriterNm() != null && !boardDto.getWriterNm().equals("")) {
            boardDto.setWriterNm(boardDto.getWriterNm());
        } else {
            boardDto.setWriterNm("비회원");
        }

        log.debug("doWriteBoardDto: " + boardDto);
        return boardMapper.doWrite(boardDto);
    }

    public int doUpdate(HashMap<String, Object> map) {
        return boardMapper.doUpdate(map);
    }

    public BoardDto getBoardView(HashMap<String, Object> map) {
        return boardMapper.getBoardView(map);
    }

    public void incrementHitCnt(HashMap<String, Object> map) {
        boardMapper.incrementHitCnt(map);
    }

    public int doDelete(HashMap<String, Object> map) {
        return boardMapper.doDelete(map);
    }

    public List<BoardDto> doSearch(HashMap<String, Object> map) {
        return boardMapper.doSearch(map);
    }

    public int doLike(HashMap<String, Object> map) {
        return boardMapper.doLike(map);
    }

    /*
     * 인기 게시글 가져오기(5개)
     * 좋아요 순으로 내림차순 정렬
     */
    public List<BoardDto> getPopularBoard(String boardId) {
        return boardMapper.getPopularBoard(boardId);
    }
}
