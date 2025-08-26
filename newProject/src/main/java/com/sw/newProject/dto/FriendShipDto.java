package com.sw.newProject.dto;

import com.sw.newProject.enumType.FriendShipStatus;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class FriendShipDto extends BaseTimeEntity{
    int toMemNo; // 보낸 사람 회원 번호

    int fromMemNo; // 받는 사람 회원 번호

    FriendShipStatus status; // 친구 요청 상태(ACCEPT: 수락, REJECT: 거절, REQUEST: 요청)

    public void acceptRequest() {
        status = FriendShipStatus.ACCEPT;
    }

    public void rejectRequest() {
        status = FriendShipStatus.REJECT;
    }
}
