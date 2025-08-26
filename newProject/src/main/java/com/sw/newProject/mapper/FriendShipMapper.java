package com.sw.newProject.mapper;

import com.sw.newProject.dto.FriendShipDto;
import com.sw.newProject.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FriendShipMapper {

    List<MemberDto> getSentRequest(int memNo);

    void createRequest(FriendShipDto friendShipDto);

    List<MemberDto> getReceivedRequest(int memNo);

    void changeStatus(FriendShipDto friendShipDto);

    int getRequest(FriendShipDto friendShipDto);

    int getAlreadyAccept(FriendShipDto friendShipDto);

    int getAlreadyReject(FriendShipDto friendShipDto);

    String getFriendList(int memNo);

    void deleteFriendToMember(FriendShipDto friendShipDto);

    void deleteFriendFromMember(FriendShipDto friendShipDto);

    void cancelRequest(FriendShipDto friendShipDto);

    String getStatus(FriendShipDto friendShipDto);

    void addFriendToMember(FriendShipDto friendShipDto);

    void addFriendFromMember(FriendShipDto friendShipDto);
}
