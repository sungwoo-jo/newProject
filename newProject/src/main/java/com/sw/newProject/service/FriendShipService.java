package com.sw.newProject.service;

import com.sw.newProject.dto.FriendShipDto;
import com.sw.newProject.enumType.ErrorCode;
import com.sw.newProject.exception.CustomException;
import com.sw.newProject.mapper.FriendShipMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendShipService {

    private final FriendShipMapper friendShipMapper;

    public List<FriendShipDto> getSentRequest(int memNo) {
        return friendShipMapper.getSentRequest(memNo);
    }

    public void createRequest(FriendShipDto friendShipDto) {
        if (getRequest(friendShipDto) < 1) {
            friendShipMapper.createRequest(friendShipDto);
        } else {
            throw new CustomException(ErrorCode.ALREADY_FRIEND_LIST, "이미 존재하는 친구 요청입니다.");
        }
    }

    public List<FriendShipDto> getReceivedRequest(int memNo) {
        return friendShipMapper.getReceivedRequest(memNo);
    }

    public void acceptRequest(FriendShipDto friendShipDto) {
        if (getAlreadyAccept(friendShipDto) < 1) { // 수락 가능한 상태
            friendShipDto.acceptRequest();
            changeStatus(friendShipDto);
        } else { // 이미 수락이 되어 있거나 거절이 된 적이 있다면
            throw new CustomException(ErrorCode.IMPOSSIBLE_REQUEST, "친구 수락이 불가한 상태입니다.(status가 REQUEST이거나, REJECT입니다.)");
        }
    }

    public void rejectRequest(FriendShipDto friendShipDto) {
        if (getAlreadyReject(friendShipDto) < 1) {
            friendShipDto.rejectRequest();
            changeStatus(friendShipDto);
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

    public int getAlreadyReject(FriendShipDto friendShipDto) { // 이미 거절이 되어 있는 상태인지 조회
        return friendShipMapper.getAlreadyReject(friendShipDto);
    }

    public void changeStatus(FriendShipDto friendShipDto) {
        friendShipMapper.changeStatus(friendShipDto);
    }
}
