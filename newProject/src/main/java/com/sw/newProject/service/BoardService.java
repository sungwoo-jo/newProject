package com.sw.newProject.service;

import com.sw.newProject.dto.BoardDto;
import com.sw.newProject.dto.PageDto;
import com.sw.newProject.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;

    public List<BoardDto> getBoardList(HashMap<String, Object> map) {
        return boardMapper.getBoardList(map);
    }

    public int getBoardCount(String boardId) {
        return boardMapper.getBoardCount(boardId);
    }

    public int getBoardSearchCount(HashMap<String, Object> map) {
        return boardMapper.getBoardSearchCount(map);
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

    public PageDto Paging(String page, int pageLength, int totalRows) {
        PageDto pageDto = new PageDto();
        pageDto.setPageLength(pageLength);
        pageDto.setTotalRows(totalRows);
        try {
            pageDto.setCurrentPage(Integer.parseInt(page));
        } catch (NumberFormatException e) {
            pageDto.setCurrentPage(1);
        }

        pageDto.setTotalPage(pageDto.getTotalRows() % pageDto.getPageLength() == 0 ?
                pageDto.getTotalRows() / pageDto.getPageLength() :
                (pageDto.getTotalRows() / pageDto.getPageLength()) + 1);

        if (pageDto.getCurrentPage() > pageDto.getTotalPage() || pageDto.getCurrentPage() <= 0) {
            pageDto.setCurrentPage(1);
        }

        pageDto.setCurrentBlock(pageDto.getCurrentPage() % pageDto.getPageLength() == 0 ?
                pageDto.getCurrentPage() / pageDto.getPageLength() :
                (pageDto.getCurrentPage() / pageDto.getPageLength()) + 1);

        pageDto.setStartPage((pageDto.getCurrentBlock() - 1) * pageDto.getPageLength() + 1);
        pageDto.setEndPage(pageDto.getStartPage() + pageDto.getPageLength() - 1);

        if (pageDto.getEndPage() > pageDto.getTotalPage()) {
            pageDto.setEndPage(pageDto.getTotalPage());
        }

        pageDto.setStart((pageDto.getCurrentPage() - 1) * pageDto.getPageLength());

        return pageDto;
    }
}
