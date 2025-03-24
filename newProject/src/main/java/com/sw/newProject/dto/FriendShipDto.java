package com.sw.newProject.dto;

import com.sw.newProject.enumType.FriendShipStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class FriendShipDto {
    int toMemNo; // 보낸 사람 회원 번호

    int fromMemNo; // 받는 사람 회원 번호

    FriendShipStatus status; // 친구 요청 상태(ACCEPT: 수락, REJECT: 거절, REQUEST: 요청)

    private LocalDateTime regDt; // 요청일시

    private LocalDateTime modDt; // 수정일시

    public void acceptRequest() {
        status = FriendShipStatus.ACCEPT;
    }

    public void rejectRequest() {
        status = FriendShipStatus.REJECT;
    }
}
