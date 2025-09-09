package com.sw.newProject.service;

import com.sw.newProject.dto.*;
import com.sw.newProject.dto.board.BoardDto;
import com.sw.newProject.dto.board.BoardListDto;
import com.sw.newProject.dto.board.BoardSearchDto;
import com.sw.newProject.enumType.NotificationType;
import com.sw.newProject.kafka.NotificationProducer;
import com.sw.newProject.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;
    private final NotificationProducer notificationProducer;

    public List<BoardDto> getBoardList(BoardListDto boardListDto) {
        return boardMapper.getBoardList(boardListDto);
    }

    public int getBoardCount(String boardId) {
        return boardMapper.getBoardCount(boardId);
    }

    public int getBoardSearchCount(BoardSearchDto boardSearchDto) {
        return boardMapper.getBoardSearchCount(boardSearchDto);
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

    public int doUpdate(BoardDto boardDto) {
        return boardMapper.doUpdate(boardDto);
    }

    public BoardDto getBoardView(BoardDto boardDto) {
        return boardMapper.getBoardView(boardDto);
    }

    /**
     * 조회수 증가 처리
     * @param boardDto
     */
    public void incrementHitCnt(BoardDto boardDto) {
        Integer result = viewHitCnt(boardDto); // 해당 회원이 오늘 게시글을 조회했었는지 여부를 확인
        log.info("result: " + result);

        if (result <= 0) { // 오늘 조회한 적이 없다면 조회수 증가 처리
            insertHitInfo(boardDto); // 조회수 중복을 방지하기 위한 데이터를 삽입
            boardMapper.incrementHitCnt(boardDto); // 조회수 1 증가
        }
    }

    public void insertHitInfo(BoardDto boardDto) {
        boardMapper.insertHitInfo(boardDto);
    }

    public Integer viewHitCnt(BoardDto boardDto) {
        return boardMapper.viewHitCnt(boardDto);
    }

    public int doDelete(BoardDto boardDto) {
        return boardMapper.doDelete(boardDto);
    }

    public List<BoardDto> doSearch(BoardSearchDto boardSearchDto) {
        return boardMapper.doSearch(boardSearchDto);
    }

    /**
     * 좋아요 처리
     * @param dto 게시글 정보, 좋아요 누른 사용자 정보가 들어가있다.
     * @return 좋아요 처리가 완료되면 게시글 작성자에게 알림을 전송한다.
     */
    public int doLike(LikeDto dto,
                      MemberDto member) {
        NotificationDto notificationDto = new NotificationDto();
        dto.getBoardDto().setMemNo(getWriterMemNo(dto.getBoardDto()));

        // 알림 전송
        notificationDto.setToMemNo(dto.getLikerMemNo());
        notificationDto.setFromMemNo(dto.getBoardDto().getMemNo());
        notificationDto.setNotificationType(NotificationType.BOARD_LIKE);
        notificationDto.setContent(member.getMemNm() + "님이 내 글을 좋아합니다.");
        notificationDto.setUrl("/board/" + dto.getBoardDto().getBoardId() + "/view/" + dto.getBoardDto().getBoardNo());

        notificationProducer.sendNotification(notificationDto);

        return boardMapper.doLike(dto);
    }

    private int getWriterMemNo(BoardDto boardDto) {
        return boardMapper.getWriterMemNo(boardDto);
    }

    /**
     * 인기 게시글 가져오기
     * @param boardId 게시판 id
     * @return 좋아요 순으로 내림차순 정렬한 인기 게시글을 5개 가져온다.
     */
    public List<BoardDto> getPopularBoard(String boardId) {
        return boardMapper.getPopularBoard(boardId);
    }
}
