package com.sw.newProject.service;

import com.sw.newProject.dto.BoardDto;
import com.sw.newProject.mapper.BoardMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class BoardService {

    private final BoardMapper boardMapper;

    public BoardService(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

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

    public int doDelete(HashMap<String, Object> map) {
        return boardMapper.doDelete(map);
    }

    public List<BoardDto> doSearch(HashMap<String, Object> map) {
        return boardMapper.doSearch(map);
    }
}
