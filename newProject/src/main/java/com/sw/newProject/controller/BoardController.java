package com.sw.newProject.controller;

import com.sw.newProject.annotation.LoginMember;
import com.sw.newProject.dto.*;
import com.sw.newProject.service.BoardService;
import com.sw.newProject.service.FriendShipService;
import com.sw.newProject.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
                               GetBoardListDto dto,
                               Model model) {
        int totalRows = boardService.getBoardCount(boardId); // 전체 게시글 갯수
        PageDto pageDto = boardService.Paging(page, 10, totalRows); // 10개씩 페이징
        dto.setBoardId(boardId);
        dto.setPageDto(pageDto);
        List<BoardDto> boardDto = boardService.getBoardList(dto);
        model.addAttribute("boardDto", boardDto);
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
                               HttpSession session,
                               Model model) {
        log.info("boardNo: " + boardNo);
        MemberDto memberDto = (MemberDto) session.getAttribute("member");
        if (boardNo != null) { // 게시글 수정 형식으로 전달
            HashMap<String, Object> map = new HashMap<>();

            map.put("boardId", boardId);
            map.put("boardNo", boardNo);

            BoardDto boardDto = boardService.getBoardView(map);
            model.addAttribute("boardDto", boardDto);
            return "board/write";
        }
        model.addAttribute("boardId", boardId);
        model.addAttribute("writerNm", memberDto.getMemNm());
        log.info("memNm: " + session.getAttribute("memNm"));
        log.info("write 페이지를 가져옵니다.");
        return "board/write";
    }

    /**
     * 게시글 상세보기 페이지 호출
     * @param referer 목록으로 돌아가기 위한 레퍼러
     * @param boardId 게시판 id
     * @param boardNo 게시글 번호
     * @param model 게시글 정보
     * @param session 세션 정보
     * @param request ip 주소 저장을 위한 값
     * @return 게시글 정보를 담아 반환
     */
    @GetMapping("{boardId}/view/{boardNo}")
    public String getViewPage(@RequestHeader (value = "Referer", defaultValue = "/") String referer, @PathVariable String boardId, @PathVariable Integer boardNo, Model model, HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        MemberDto memberDto = (MemberDto) session.getAttribute("member");
        String ip = request.getRemoteAddr(); // 게시글 조회자 ip 저장

        map.put("boardId", boardId);
        map.put("boardNo", boardNo);
        map.put("memNo", memberDto.getMemNo());
        map.put("ip", ip);

        boardService.incrementHitCnt(map); // 조회수 증가
        BoardDto boardDto = boardService.getBoardView(map);

        HashMap<String, String> followDataMap = memberService.getFollowData(memberDto.getMemNo()); // 회원의 현재 팔로우 데이터를 가져오기
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
        friendShipDto.setToMemNo(memberDto.getMemNo());
        friendShipDto.setFromMemNo(boardDto.getMemNo());
        String alreadyRequestFl = friendShipService.getStatus(friendShipDto);
        log.info("alreadyRequestFl: {}", alreadyRequestFl);

        if (boardDto.getDeleteYn() != TRUE) {
            model.addAttribute("previousPage", referer);
            model.addAttribute("boardDto", boardDto);
            model.addAttribute("boardId", boardId);
            model.addAttribute("alreadyFollowFl", alreadyFollowFl);
            model.addAttribute("alreadyRequestFl", alreadyRequestFl);
            model.addAttribute("session", session);
            log.debug("boardDto: " + boardDto);
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
     * @param boardId 게시판 id
     * @param session 세션 정보
     * @return 좋아요 처리에 따라 결과값 반환
     */
    @PostMapping("{boardId}/doLike")
    public ResponseEntity<String> doLike(@RequestBody BoardDto boardDto, @PathVariable String boardId, HttpSession session) {
        log.info("boardDto: {}", boardDto);
        MemberDto memberDto = (MemberDto) session.getAttribute("member");
        HashMap<String, Object> map = new HashMap<>();
        map.put("boardDto", boardDto);
        map.put("boardId", boardId);
        map.put("memNo", memberDto.getMemNo()); // 좋아요 누른 사람
        int result = boardService.doLike(map);
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
        log.info("memNo: {}", memberDto.getMemNo());
        int result = 0;
        if (boardDto.getBoardNo() != null) { // 수정
            HashMap<String, Object> map = new HashMap<>();
            map.put("boardId", boardId);
            map.put("boardDto", boardDto);

            result = boardService.doUpdate(map);
        } else {
            result = boardService.doWrite(boardDto);
        }
        return result > 0 ? ResponseEntity.ok("success") : ResponseEntity.ok("fail");
    }

    /**
     * 게시글 삭제 처리
     * @param boardDto 게시글 정보
     * @param boardId 게시판 id
     * @return 게시글 삭제 여부에 따라 결과값 반환
     */
    @PostMapping("{boardId}/doDelete")
    public ResponseEntity<String> doDelete(@RequestBody BoardDto boardDto, @PathVariable String boardId) {
        int result = 0;
        if (boardDto.getBoardNo() != null && boardId != null) { // 삭제 진행
            HashMap<String, Object> map = new HashMap<>();
            map.put("boardDto", boardDto);
            map.put("boardId", boardId);

            result = boardService.doDelete(map);
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
            HashMap<String, Object> map = new HashMap<>();
            map.put("boardId", boardId);
            map.put("type", type);
            map.put("keyword", keyword);
            int totalRows = boardService.getBoardSearchCount(map); // 전체 게시글 갯수
            PageDto pageDto = boardService.Paging(page, 10, totalRows); // 10개씩 페이징
            map.put("pageDto", pageDto);
            List<BoardDto> boardDto = boardService.doSearch(map);
            model.addAttribute("boardDto", boardDto);
            model.addAttribute("pageDto", pageDto);
        }
        // todo: 검색어가 없을 경우 알럿창 띄우기
        return "board/list";
    }
}
