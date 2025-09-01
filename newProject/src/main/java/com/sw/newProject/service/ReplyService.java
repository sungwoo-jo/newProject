package com.sw.newProject.service;

import com.sw.newProject.dto.NotificationDto;
import com.sw.newProject.dto.ReplyDto;
import com.sw.newProject.enumType.NotificationType;
import com.sw.newProject.kafka.NotificationProducer;
import com.sw.newProject.mapper.ReplyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyMapper replyMapper;
    private final MemberService memberService;
    private final NotificationProducer notificationProducer;

    public List<ReplyDto> getReply(HashMap<String, Object> map) {
        return replyMapper.getReply(map);
    }

    public void doWrite(ReplyDto replyDto) {
        NotificationDto notificationDto = new NotificationDto();
        replyMapper.doWrite(replyDto);

        // 알림 전송
        notificationDto.setToMemNo(replyDto.getMemNo());
        notificationDto.setFromMemNo(getWriterNo(replyDto.getBoardNo(), replyDto.getBoardId()));
        notificationDto.setNotificationType(NotificationType.REPLY);
        notificationDto.setContent(memberService.getMember(replyDto.getMemNo()).getMemNm() + "님의 새로운 댓글이 있습니다.");
        notificationDto.setUrl("/board/" + replyDto.getBoardId() + "/view/" + replyDto.getBoardNo());
        notificationProducer.sendNotification(notificationDto);
    }

    public void doDelete(HashMap<String, Object> map) {
        replyMapper.doDelete(map);
    }

    public void doUpdate(HashMap<String, Object> map) {
        replyMapper.doUpdate(map);
    }

    /*
     * 게시판명과 게시글 번호로 게시글 작성자 회원 번호를 가져온다.
     */
    public Integer getWriterNo(Integer boardNo, String boardId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("boardNo", boardNo);
        map.put("boardId", boardId);
        return replyMapper.getWriterNo(map);
    }
}
