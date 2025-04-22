package com.sw.newProject.service;

import com.sw.newProject.dto.BoardDto;
import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.dto.NotificationDto;
import com.sw.newProject.dto.PageDto;
import com.sw.newProject.enumType.NotificationType;
import com.sw.newProject.mapper.BoardMapper;
import jakarta.servlet.http.HttpSession;
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
    private final MemberService memberService;
    private final NotificationService notificationService;

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
        BoardDto boardDto = (BoardDto) map.get("boardDto");
        Integer boardNo = boardDto.getBoardNo(); // 대상 게시글 번호
        map.put("boardNo", boardNo);
        MemberDto memberDto = memberService.getMember((Integer) map.get("memNo")); // 좋아요 누른 사람
        Integer writerNo = getMemNoByBoardNo(map); // 작성자 정보 가져오기
        String boardId = (String) map.get("boardId");

        NotificationDto notificationDto = new NotificationDto();
        // 알림 전송
        notificationDto.setToMemNo(writerNo);
        notificationDto.setFromMemNo(memberDto.getMemNo());
        notificationDto.setNotificationType(NotificationType.BOARD_LIKE);
        notificationDto.setContent(memberDto.getMemNm() + "님이 내 글을 좋아합니다.");
        notificationDto.setUrl("/board/" + boardId + "/view/" + boardNo);

        notificationService.notifyOne(notificationDto.getToMemNo(), notificationDto.getContent(), notificationDto.getNotificationType());
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

    public Integer getMemNoByBoardNo(HashMap<String, Object> map) {
        return boardMapper.getMemNoByBoardNo(map);
    }
}
