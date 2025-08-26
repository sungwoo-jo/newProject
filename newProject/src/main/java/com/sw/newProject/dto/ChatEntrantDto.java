package com.sw.newProject.dto;

import com.sw.newProject.enumType.ChatEntrantStatus;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class ChatEntrantDto extends BaseTimeEntity{
    private Integer sno; // chatinfo.sno

    private Integer entrant; // 참여자 memNo

    private ChatEntrantStatus status; // 요청 수락 상태(수락, 거절, 요청)

    public void acceptRequest() {
        status = ChatEntrantStatus.ACCEPT;
    }

    public void rejectRequest() {
        status = ChatEntrantStatus.REJECT;
    }
}
