package com.sw.newProject.service;

import com.sw.newProject.dto.NotificationDto;
import com.sw.newProject.dto.PostDto;
import com.sw.newProject.enumType.NotificationType;
import com.sw.newProject.kafka.NotificationProducer;
import com.sw.newProject.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostMapper postMapper;
    private final NotificationProducer notificationProducer;

    public List<PostDto> getPostList(Integer memNo) {
        return postMapper.getPostList(memNo);
    }

    public PostDto viewPost(Integer postNo) {
        PostDto postDto = postMapper.viewPost(postNo);
        NotificationDto notificationDto = new NotificationDto();
        if (!postDto.getReadYn()) {
            updateReadYn(postNo);
            // 보낸 사람에게 알림 전송
            notificationDto.setToMemNo(postDto.getReceiverMemNo());
            notificationDto.setFromMemNo(postDto.getSenderMemNo());
            notificationDto.setContent(postDto.getReceiverMemId() + "님이 쪽지를 읽었습니다.");
            notificationDto.setUrl("/post/list");
            notificationDto.setNotificationType(NotificationType.POST_READ);
            notificationProducer.sendNotification(notificationDto);
        }
        return postDto;
    }

    public Integer doDelete(Integer postNo) {
        return postMapper.doDelete(postNo);
    }

    public Integer doWrite(PostDto postDto) {
        return postMapper.doWrite(postDto);
    }

    public Integer getMemNoOfMemId(String memId) {
        return postMapper.getMemNoOfMemId(memId);
    }

    /**
     * 읽음 여부 및 읽은 시간 갱신
     *
     * @param postNo   - 쪽지의 번호
     */
    public void updateReadYn(Integer postNo) {
        postMapper.updateReadYn(postNo);
    }
}
