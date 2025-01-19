package com.sw.newProject.service;

import com.sw.newProject.dto.BoardDto;
import com.sw.newProject.mapper.BoardMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardMapper boardMapper;

    public BoardService(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    public List<BoardDto> getBoardList(String boardId) {
        return boardMapper.getBoardList(boardId);
    }
}
