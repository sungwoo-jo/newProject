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

    /**
     * 게시글 작성 처리(회원이 글을 작성할 때는 해당 회원의 회원번호로 세팅)
     * @param boardDto 게시글 정보
     * @return
     */
    public int doWrite(BoardDto boardDto) {
        int result;
        if (boardDto.getMemNo() != null && !boardDto.getMemNo().equals("")) { // 회원이 글을 작성할 때는 해당 회원의 번호와 이름으로 세팅
            boardDto.setMemNo(boardDto.getMemNo());
            boardDto.setWriterNm(boardDto.getWriterNm());
        } else { // 비회원 글 작성 시 회원번호는 0으로, 이름은 비회원으로 세팅한다.
            boardDto.setMemNo(0);
            boardDto.setWriterNm("비회원");
        }
        result = boardMapper.doWrite(boardDto);
        return result;
    }

    public int doUpdate(HashMap<String, Object> map) {
        return boardMapper.doUpdate(map);
    }

    public BoardDto getBoardView(HashMap<String, Object> map) {
        return boardMapper.getBoardView(map);
    }

    /**
     * 조회수 증가 처리
     * @param map 게시글 상세 보기 시 호출되며 게시판 id, 게시글 번호, 회원번호, ip 정보가 들어있다.
     */
    public void incrementHitCnt(HashMap<String, Object> map) {
        Integer result = viewHitCnt(map); // 해당 회원이 오늘 게시글을 조회했었는지 여부를 확인
        log.info("result: " + result);

        if (result <= 0) { // 오늘 조회한 적이 없다면 조회수 증가 처리
            insertHitInfo(map); // 조회수 중복을 방지하기 위한 데이터를 삽입
            boardMapper.incrementHitCnt(map); // 조회수 1 증가
        }
    }

    public void insertHitInfo(HashMap<String, Object> map) {
        boardMapper.insertHitInfo(map);
    }

    public Integer viewHitCnt(HashMap<String, Object> map) {
        return boardMapper.viewHitCnt(map);
    }

    public int doDelete(HashMap<String, Object> map) {
        return boardMapper.doDelete(map);
    }

    public List<BoardDto> doSearch(HashMap<String, Object> map) {
        return boardMapper.doSearch(map);
    }

    /**
     * 좋아요 처리
     * @param map 게시글 정보, 게시판 id, 좋아요 누른 사용자 정보가 들어가있다.
     * @return 좋아요 처리가 완료되면 게시글 작성자에게 알림을 전송한다.
     */
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

    /**
     * 인기 게시글 가져오기
     * @param boardId 게시판 id
     * @return 좋아요 순으로 내림차순 정렬한 인기 게시글을 5개 가져온다.
     */
    public List<BoardDto> getPopularBoard(String boardId) {
        return boardMapper.getPopularBoard(boardId);
    }

    /**
     * 게시글 조회 시 전역으로 사용되는 페이징 함수
     * @param page 현재 페이지 번호
     * @param pageLength 페이징 할 갯수
     * @param totalRows 총 페이지의 수
     * @return 페이징 정보를 반환
     */
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
