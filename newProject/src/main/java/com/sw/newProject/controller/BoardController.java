package com.sw.newProject.controller;

import com.sw.newProject.annotation.LoginMember;
import com.sw.newProject.dto.*;
import com.sw.newProject.dto.board.BoardDto;
import com.sw.newProject.dto.board.BoardListDto;
import com.sw.newProject.dto.board.BoardSearchDto;
import com.sw.newProject.dto.like.LikeDto;
import com.sw.newProject.service.BoardService;
import com.sw.newProject.service.FriendShipService;
import com.sw.newProject.service.MemberService;
import com.sw.newProject.service.PageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final FriendShipService friendShipService;
    private final PageService pageService;

    /**
     * 게시글 리스트 조회
     * @param boardId 게시글 번호
     * @param model 게시글 리스트 반환 시 사용할 model
     * @param page 페이지 번호
     * @return 게시글 리스트를 담아 게시글 리스트 페이지를 반환
     */
    @GetMapping("{boardId}/list")
    public String getBoardList(@PathVariable String boardId,
                               @RequestParam(defaultValue = "1") String page,
                               BoardDto boardDto,
                               Model model) {
        BoardListDto boardListDto = new BoardListDto();
        boardDto.setBoardId(boardId);
        int totalRows = boardService.getBoardCount(boardDto.getBoardId()); // 전체 게시글 갯수
        PageDto pageDto = pageService.Paging(page, 10, totalRows); // 10개씩 페이징

        boardListDto.setBoardDto(boardDto);
        boardListDto.setPageDto(pageDto);
        List<BoardDto> boardList = boardService.getBoardList(boardListDto);
        model.addAttribute("boardDto", boardList);
        model.addAttribute("pageDto", pageDto);
        return "board/list";
    }

    /**
     * 인기게시글 조회 처리
     * @param boardId 게시판 id
     * @return 인기게시글 목록을 반환
     */
    @ResponseBody
    @GetMapping("{boardId}/getPopularBoard")
    public List<BoardDto> getPopularBoard(@PathVariable String boardId) {
        return boardService.getPopularBoard(boardId);
    }

    /**
     * 게시글 작성 페이지 호출
     * @param boardId 게시판 id
     * @param boardNo 게시글 번호(파라미터에 게시글 번호가 없으면 신규 게시글 등록, 게시글 번호가 있으면 기존 게시글 수정 처리)
     * @param model 게시글 수정 시 가져올 기존 작성된 게시글 정보
     * @return 게시글 작성 페이지 반환
     */
    @GetMapping(value = {"{boardId}/write/{boardNo}", "{boardId}/write", "{boardId}/write/"})
    public String getWritePage(@PathVariable String boardId,
                               @PathVariable(value = "boardNo", required = false) Integer boardNo,
                               @LoginMember MemberDto member,
                               Model model) {
        BoardDto boardDto = new BoardDto();
        log.info("boardNo: " + boardNo);
        log.info("memNo: " + member.getMemNo());
        boardDto.setBoardId(boardId);
        if (boardNo != null) { // 게시글 수정 형식으로 전달
            boardDto.setBoardNo(boardNo);

            boardDto = boardService.getBoardView(boardDto);
            log.info("writerNm: {}", boardDto.getWriterNm());
            model.addAttribute("boardDto", boardDto);
            return "board/write";
        }
        boardDto.setBoardId(boardId);
        boardDto.setWriterNm(member.getMemNm());
        model.addAttribute("boardDto", boardDto);
        log.info("writerNm: {}", member.getMemNm());
        return "board/write";
    }

    /**
     * 게시글 상세보기 페이지 호출
     * @param referer 목록으로 돌아가기 위한 레퍼러
     * @param boardId 게시판 id
     * @param boardNo 게시글 번호
     * @param model 게시글 정보
     * @param member 세션 정보
     * @param request ip 주소 저장을 위한 값
     * @return 게시글 정보를 담아 반환
     */
    @GetMapping("{boardId}/view/{boardNo}")
    public String getViewPage(@RequestHeader (value = "Referer", defaultValue = "/") String referer,
                              @PathVariable String boardId,
                              @PathVariable Integer boardNo,
                              @LoginMember MemberDto member,
                              Model model,
                              HttpServletRequest request) {
        BoardDto boardDto = new BoardDto();
        String writerIp = request.getRemoteAddr(); // 게시글 작성자 ip 저장

        boardDto.setBoardNo(boardNo);
        log.info("boardId: {}", boardId);
        log.info("boardNo: {}", boardNo);
        boardDto.setMemNo(member.getMemNo());
        boardDto.setWriterIp(writerIp);
        boardDto.setBoardId(boardId);

        boardService.incrementHitCnt(boardDto); // 조회수 증가
        BoardDto currentBoardDto = boardService.getBoardView(boardDto);
        currentBoardDto.setBoardId(boardId);

        HashMap<String, String> followDataMap = memberService.getFollowData(member.getMemNo()); // 회원의 현재 팔로우 데이터를 가져오기
        JSONObject prevFollowData = new JSONObject(followDataMap);
        String followDataJson = prevFollowData.getString("follow");
        JSONObject followData = new JSONObject(followDataJson);

        boolean alreadyFollowFl;

        if (followData.has(String.valueOf(boardDto.getMemNo()))) {
            alreadyFollowFl = TRUE;
        } else {
            alreadyFollowFl = FALSE;
        }

        FriendShipDto friendShipDto = new FriendShipDto();
        friendShipDto.setToMemNo(member.getMemNo());
        friendShipDto.setFromMemNo(boardDto.getMemNo());
        String alreadyRequestFl = friendShipService.getStatus(friendShipDto);
        log.info("alreadyRequestFl: {}", alreadyRequestFl);


        if (boardDto.getDeleteYn() != TRUE) {
            model.addAttribute("previousPage", referer);
            model.addAttribute("boardDto", currentBoardDto);
            model.addAttribute("alreadyFollowFl", alreadyFollowFl);
            model.addAttribute("alreadyRequestFl", alreadyRequestFl);
            model.addAttribute("session", member);
            log.info("prevFollowData: " + prevFollowData);
            log.info("alreadyFollowFl: " + alreadyFollowFl);
            return "board/view";
        } else { // 게시글이 삭제된 상태
//            model.addAttribute("boardId", boardId);
//            model.addAttribute("notFound", "삭제된 게시글입니다.");
            return "redirect:../list";
        }
    }

    /**
     * 좋아요 처리
     * @param boardDto 게시글 정보
     * @param member 회원 세션
     * @return 좋아요 처리에 따라 결과값 반환
     */
    @PostMapping("{boardId}/doLike")
    public ResponseEntity<String> doLike(@RequestBody BoardDto boardDto,
                                         @LoginMember MemberDto member) {
        LikeDto likeDto = new LikeDto();

        likeDto.setBoardDto(boardDto);
        likeDto.setLikerMemNo(member.getMemNo());

        int result = boardService.doLike(likeDto, member);
        return result > 0 ? ResponseEntity.ok("success") : ResponseEntity.ok("fail");
    }

    /**
     * 게시글 작성 처리
     * @param boardDto 작성할 게시글 정보
     * @param boardId 게시판 id
     * @return 게시글 작성 여부에 따라 결과값 반환
     */
    @PostMapping("{boardId}/doWrite")
    @Operation(summary = "게시글 작성 처리", description = "실제 게시글 내용을 입력받아 저장합니다.")
    public ResponseEntity<String> doWrite(@RequestBody BoardDto boardDto,
                                          @PathVariable String boardId,
                                          @LoginMember MemberDto memberDto) {
        boardDto.setMemNo(memberDto.getMemNo());
        boardDto.setWriterNm(memberDto.getMemNm());
        boardDto.setBoardId(boardId);

        int result = 0;
        if (boardDto.getBoardNo() != null) { // 수정
            result = boardService.doUpdate(boardDto);
        } else {
            result = boardService.doWrite(boardDto);
        }
        return result > 0 ? ResponseEntity.ok("success") : ResponseEntity.ok("fail");
    }

    /**
     * 게시글 삭제 처리
     * @param boardDto 게시글 정보
     * @return 게시글 삭제 여부에 따라 결과값 반환
     */
    @PostMapping("{boardId}/doDelete")
    public ResponseEntity<String> doDelete(@RequestBody BoardDto boardDto) {
        int result = 0;
        if (boardDto.getBoardNo() != null && boardDto.getBoardId() != null) { // 삭제 진행
            result = boardService.doDelete(boardDto);
        }
        return result > 0 ? ResponseEntity.ok("success") : ResponseEntity.ok("fail");
    }

    /**
     * 게시글 검색
     * @param boardId 게시판 id
     * @param model 게시글 정보 및 페이지 정보
     * @param type 검색 타입 지정(제목, 내용, 제목+내용, 작성자, 지역명, 해시태그)
     * @param keyword 검색어
     * @param page 페이징 정보
     * @return 검색된 게시글 정보를 페이징 정보와 함께 반환
     */
    @GetMapping("/{boardId}/doSearch")
    public String doSearch(@PathVariable String boardId, Model model, @RequestParam String type, @RequestParam String keyword, @RequestParam(defaultValue = "1") String page) {
        if (!type.equals("") && !keyword.equals("")) {
            BoardSearchDto boardSearchDto = new BoardSearchDto();
            log.info("boardId: {}", boardId);
            boardSearchDto.getBoardDto().setBoardId(boardId);
            boardSearchDto.setType(type);
            boardSearchDto.setKeyword(keyword);
            int totalRows = boardService.getBoardSearchCount(boardSearchDto); // 전체 게시글 갯수
            PageDto pageDto = pageService.Paging(page, 10, totalRows); // 10개씩 페이징
            boardSearchDto.setPageDto(pageDto);
            List<BoardDto> boardDto = boardService.doSearch(boardSearchDto);
            model.addAttribute("boardDto", boardDto);
            model.addAttribute("pageDto", boardSearchDto.getPageDto());
        }
        // todo: 검색어가 없을 경우 알럿창 띄우기
        return "board/list";
    }
}
