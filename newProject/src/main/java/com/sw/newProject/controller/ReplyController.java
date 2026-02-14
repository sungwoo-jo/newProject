package com.sw.newProject.controller;

import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.dto.reply.GetReplyDto;
import com.sw.newProject.dto.reply.ReplyDto;
import com.sw.newProject.dto.reply.TargetOrderDto;
import com.sw.newProject.service.ReplyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/{boardId}/getReply/{boardNo}") // 댓글 조회
    public List<GetReplyDto> getReply(ReplyDto dto) {
        List<GetReplyDto> replyDto = replyService.getReply(dto);
        return replyDto;
    }

    @PostMapping("/{boardId}/doWrite/{boardNo}") // 댓글 작성
    public void doWrite(@RequestBody ReplyDto replyDto, HttpSession session) {
        log.info("boardId: {}, boardNo: {}, parentNo: {}, contents: {}", replyDto.getBoardId(), replyDto.getBoardNo(), replyDto.getParentNo(), replyDto.getContents());

        MemberDto memberDto = (MemberDto) session.getAttribute("member");

        if (replyDto.getParentNo() == null) { // 일반 댓글을 작성하는 경우
            // 게시글 내 댓글 그룹의 가장 높은 번호를 조회 후 해당 번호에 1을 더해준다.
            Integer groupNo = replyService.getMaxGroupNo(replyDto);
            if (groupNo == null) { // 첫 댓글인 경우
                replyDto.setGroupNo(0);
            } else {
                replyDto.setGroupNo(groupNo + 1);
            }
            // 나머지는 기본값인 0으로 세팅
            replyDto.setGroupOrder(0);
            replyDto.setDepth(0);
            replyDto.setAnswerCnt(0);
        } else { // 대댓글을 작성하는 경우
            // 부모 댓글의 정보를 기준으로 값을 세팅하기 위해 먼저 조회
            ReplyDto parentReplyDto = replyService.getParentReplyInfo(replyDto.getParentNo());
            log.info("parentReplyDto: {}", parentReplyDto);

            Integer parentGroupNo = replyService.getParentGroupNo(replyDto.getParentNo()); // 부모글과 같은 그룹번호로 세팅하기 위해 부모글의 그룹 번호를 구한다
            log.info("parentGroupNo: {}", parentGroupNo);

            replyDto.setGroupNo(parentGroupNo); // 부모글과 같은 그룹으로 세팅

            TargetOrderDto targetOrderDto = new TargetOrderDto();
            targetOrderDto.setGroupNo(replyDto.getGroupNo());
            targetOrderDto.setParentGroupOrder(parentReplyDto.getGroupOrder());
            targetOrderDto.setParentDepth(parentReplyDto.getDepth());
            targetOrderDto.setTargetOrder(replyService.getTargetOrder(targetOrderDto));
            targetOrderDto.setBoardId(replyDto.getBoardId());
            targetOrderDto.setBoardNo(replyDto.getBoardNo());
//            log.info("targetOrderDto.getTargetOrder: {}", targetOrderDto.getTargetOrder());

            replyDto.setGroupOrder(targetOrderDto.getTargetOrder()); // 새로운 댓글이 들어갈 위치 세팅
            log.info("--- 업데이트 실행 전 파라미터 확인 ---");
            log.info("groupNo: {}, parentGroupOrder: {}, parentDepth: {}, targetOrder: {}", targetOrderDto.getGroupNo(), targetOrderDto.getParentGroupOrder(),
                    targetOrderDto.getParentDepth(), targetOrderDto.getTargetOrder());

            int result = replyService.updateExistingReply(targetOrderDto); // 기존 댓글들 밀어내기

            log.info("--- 업데이트 결과: {} 건의 순서가 변경됨 ---", result);
//            replyService.insertNewReply(targetOrderDto);// 새로운 댓글 삽입하기

            // 이거는 참고한 로직
//            replyDto.setGroupOrder(parentReplyDto.getGroupOrder() + 1); // 부모글의 정렬순서 +1
            replyDto.setDepth(parentReplyDto.getDepth() + 1); // 부모 댓글 기준으로 뎁스는 +1
//            // 이거는 적절하게 맞춘 로직
//            Integer maxGroupOrder = replyService.getMaxGroupOrder(replyDto);
//            replyDto.setGroupOrder(maxGroupOrder + 1);
//            replyService.plusGroupOrder(replyDto); // 이미 작성된 자식글들이 있다면 모두 +1

            replyService.updateParentAnswerCnt(replyDto.getParentNo()); // 부모글의 setAnswerCnt를 1증가
            replyDto.setAnswerCnt(0); // 자신의 자식 댓글 갯수는 0으로 세팅

        }
        replyDto.setMemNo(memberDto.getMemNo());

        replyService.doWrite(replyDto);

//        if (replyDto.getParentNo() != null) {
//            // 신규로 작성한 댓글의 groupNo와 groupOrder가 같은 댓글들의 groupOrder를 전부 +1씩 업데이트
//            plusGroupOrder(replyDto);
//        }
    }

    private Integer existBeforeReply(ReplyDto replyDto) {
        return replyService.existBeforeReply(replyDto);
    }

    private Integer existParentNo(ReplyDto parentReplyDto) {
        return replyService.existParentNo(parentReplyDto);
    }

    private Integer existGroupOrder(ReplyDto parentReplyDto) {
        return replyService.existGroupOrder(parentReplyDto);
    }

    @DeleteMapping("/{boardId}/doDelete/{replyNo}") // 댓글 삭제(softDelete)
    public void doDelete(@PathVariable String boardId, @PathVariable Integer replyNo) {
        log.debug("replyNo: " + replyNo);
        HashMap<String, Object> map = new HashMap<>();
        map.put("boardId", boardId);
        map.put("replyNo", replyNo);
        log.info("{}", map);
        replyService.doDelete(map);
    }

    @PatchMapping("/{boardId}/doUpdate/{replyNo}") // 댓글 수정
    public void doUpdate(@PathVariable String boardId, @PathVariable Integer replyNo, @RequestBody String contents) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("boardId", boardId);
        map.put("replyNo", replyNo);
        map.put("contents", contents);
        log.info("{}", map);
        replyService.doUpdate(map);
    }
}
