package com.sw.newProject.mapper;

import com.sw.newProject.dto.FriendShipDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FriendShipMapper {

    List<FriendShipDto> getSentRequest(int memNo);

    void createRequest(FriendShipDto friendShipDto);

    List<FriendShipDto> getReceivedRequest(int memNo);

    void changeStatus(FriendShipDto friendShipDto);

    int getRequest(FriendShipDto friendShipDto);

    int getAlreadyAccept(FriendShipDto friendShipDto);

    int getAlreadyReject(FriendShipDto friendShipDto);

    String getFriendList(int memNo);

    void updateFriendList(int memNo, String friendListJson);

    void deleteFriendToMember(FriendShipDto friendShipDto);

    void deleteFriendFromMember(FriendShipDto friendShipDto);

    void deleteRequest(FriendShipDto friendShipDto);

    String getStatus(FriendShipDto friendShipDto);
}
