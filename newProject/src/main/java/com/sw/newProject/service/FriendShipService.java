package com.sw.newProject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sw.newProject.dto.FriendShipDto;
import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.dto.NotificationDto;
import com.sw.newProject.enumType.ErrorCode;
import com.sw.newProject.enumType.NotificationType;
import com.sw.newProject.exception.CustomException;
import com.sw.newProject.mapper.FriendShipMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendShipService {

    private final FriendShipMapper friendShipMapper;
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;
    private final MemberService memberService;

    NotificationDto notificationDto = new NotificationDto(); // 알림 전송 객체 선언

    public List<MemberDto> getSentRequest(int memNo) {
        return friendShipMapper.getSentRequest(memNo);
    }

    public void createRequest(FriendShipDto friendShipDto) {
        if (getRequest(friendShipDto) < 1) {
            friendShipMapper.createRequest(friendShipDto); // 요청 전송

            log.info("friendShipDto: {}", friendShipDto);

            // 알림 발송 & 알림 발송 내역 저장
            saveNotifyAndSend(friendShipDto, NotificationType.FRIEND_REQ);
        } else {
            throw new CustomException(ErrorCode.ALREADY_FRIEND_LIST, "이미 존재하는 친구 요청입니다.");
        }
    }

    public void saveNotifyAndSend(FriendShipDto friendShipDto, NotificationType type) {
        // 알림 dto 세팅

        notificationDto.setNotificationType(type);
        switch (type) {
            case FRIEND_REQ -> {
                notificationDto.setToMemNo(friendShipDto.getToMemNo());
                notificationDto.setFromMemNo(friendShipDto.getFromMemNo());
                notificationDto.setContent(memberService.getMember(notificationDto.getToMemNo()).getMemNm() + "님이 친구 요청을 보냈습니다.");
                notificationDto.setUrl("/mypage/friendList");
            }
            case FRIEND_ACC -> {
                notificationDto.setFromMemNo(friendShipDto.getToMemNo());
                notificationDto.setToMemNo(friendShipDto.getFromMemNo());
                notificationDto.setContent(memberService.getMember(notificationDto.getToMemNo()).getMemNm() + "님이 친구 요청을 수락했습니다.");
                notificationDto.setUrl("/mypage/friendList");
            }
            case FRIEND_REJ -> {
                notificationDto.setFromMemNo(friendShipDto.getToMemNo());
                notificationDto.setToMemNo(friendShipDto.getFromMemNo());
                notificationDto.setContent(memberService.getMember(notificationDto.getToMemNo()).getMemNm() + "님이 친구 요청을 거절했습니다.");
                notificationDto.setUrl("/mypage/friendList");
            }
        }
        log.info("notificationDt: {}", notificationDto);

        // 알림 내역 저장
        notificationService.saveNotify(notificationDto);
        notificationService.notifyOne(notificationDto.getFromMemNo(), notificationDto.getContent(), notificationDto.getNotificationType());
    }

    public List<MemberDto> getReceivedRequest(int memNo) {
        return friendShipMapper.getReceivedRequest(memNo);
    }

    public void acceptRequest(FriendShipDto friendShipDto) throws JsonProcessingException {
        if (getAlreadyAccept(friendShipDto) < 1) { // 수락 가능한 상태
            if (getRequest(friendShipDto) > 0) { // 보낸 요청이 있어 수락 가능
                friendShipDto.acceptRequest();
                changeStatus(friendShipDto);
                addFriendList(friendShipDto); // 서로의 친구 리스트에 추가

                saveNotifyAndSend(friendShipDto, NotificationType.FRIEND_ACC); // 알림 발송 & 알림 발송 내역 저장
            } else {
                throw new CustomException(ErrorCode.NOT_EXISTED_REQUEST, "보낸 친구 요청이 없습니다.");
            }
        } else { // 이미 수락이 되어 있거나 거절이 된 적이 있다면
            throw new CustomException(ErrorCode.IMPOSSIBLE_REQUEST, "친구 수락이 불가한 상태입니다.(이미 수락되었거나, 거절 되었습니다.)");
        }
    }

    public void rejectRequest(FriendShipDto friendShipDto) {
        if (getAlreadyReject(friendShipDto) < 1) {
            if (getRequest(friendShipDto) > 0) {
                friendShipDto.rejectRequest();
                changeStatus(friendShipDto);
                saveNotifyAndSend(friendShipDto, NotificationType.FRIEND_REJ); // 알림 발송 & 알림 발송 내역 저장
            } else {
                throw new CustomException(ErrorCode.NOT_EXISTED_REQUEST, "보낸 친구 요청이 없습니다.");
            }
        } else {
            throw new CustomException(ErrorCode.IMPOSSIBLE_REJECT, "거절이 불가한 상태입니다.(이미 거절된 적 있거나, 친구 요청을 보낸 적이 없습니다.)");
        }
    }

    public int getRequest(FriendShipDto friendShipDto) { // toMemNo와 fromMemNo로 조회
        return friendShipMapper.getRequest(friendShipDto);
    }

    public int getAlreadyAccept(FriendShipDto friendShipDto) { // 이미 수락이 되어 있는 상태인지 조회
        return friendShipMapper.getAlreadyAccept(friendShipDto);
    }

    public String getStatus(FriendShipDto friendShipDto) {
        return friendShipMapper.getStatus(friendShipDto);
    }

    public int getAlreadyReject(FriendShipDto friendShipDto) { // 이미 거절이 되어 있는 상태인지 조회
        return friendShipMapper.getAlreadyReject(friendShipDto);
    }

    public void changeStatus(FriendShipDto friendShipDto) {
        friendShipMapper.changeStatus(friendShipDto);
    }

    public void addFriendList(FriendShipDto friendShipDto) throws JsonProcessingException { // 친구 리스트에 추가
        Date date = new Date();
        SimpleDateFormat followDt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = followDt.format(date);

        // 요청자의 친구 리스트에 추가한다.
        String toMemberFriendListJson = getFriendList(friendShipDto.getToMemNo());
        JsonNode toMemberFriendList = objectMapper.readTree(toMemberFriendListJson);

        try {
            // 새로운 데이터 추가
            ((ObjectNode) toMemberFriendList).put(String.valueOf(friendShipDto.getFromMemNo()), now);

            // JSON을 문자열로 변환 후 DB 업데이트
            friendShipMapper.updateFriendList(friendShipDto.getToMemNo(), toMemberFriendList.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 수락자의 친구 리스트에 추가한다.
        String fromMemberFriendListJson = getFriendList(friendShipDto.getFromMemNo());
        JsonNode fromMemberFriendList = objectMapper.readTree(fromMemberFriendListJson);

        try {
            // 새로운 데이터 추가
            ((ObjectNode) fromMemberFriendList).put(String.valueOf(friendShipDto.getToMemNo()), now);

            // JSON을 문자열로 변환 후 DB 업데이트
            friendShipMapper.updateFriendList(friendShipDto.getFromMemNo(), fromMemberFriendList.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFriendToMember(FriendShipDto friendShipDto) { // toMemNo의 친구목록에서 fromMemNo 삭제하기
        friendShipMapper.deleteFriendToMember(friendShipDto);
    }

    public void deleteFriendFromMember(FriendShipDto friendShipDto) { // fromMemNo의 친구목록에서 toMemNo 삭제하기
        friendShipMapper.deleteFriendFromMember(friendShipDto);
    }

    public void deleteFriend(FriendShipDto friendShipDto) { // 서로의 친구 목록에서 삭제
        deleteFriendToMember(friendShipDto);
        deleteFriendFromMember(friendShipDto);
        cancelRequest(friendShipDto); // 친구 요청 목록도 삭제해서 다시 요청할 수 있는 상태를 만듬
    }

    public void cancelRequest(FriendShipDto friendShipDto) { // 친구 요청 목록 삭제
        friendShipMapper.cancelRequest(friendShipDto);
    }

    public String getFriendList(int memNo) {
        return friendShipMapper.getFriendList(memNo);
    }
}
