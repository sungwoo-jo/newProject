package com.sw.newProject.dto;

import com.sw.newProject.enumType.ChatEntrantStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ChatEntrantDto {
    private Integer sno; // chatinfo.sno

    private Integer entrant; // 참여자 memNo

    private ChatEntrantStatus status; // 요청 수락 상태(수락, 거절, 요청)

    private LocalDateTime regDt; // 초대일시

    private LocalDateTime modDt; // 수정일시

    public void acceptRequest() {
        status = ChatEntrantStatus.ACCEPT;
    }

    public void rejectRequest() {
        status = ChatEntrantStatus.REJECT;
    }
}
