package com.sw.newProject.mapper;

import com.sw.newProject.dto.AppendTargetToJsonDto;
import com.sw.newProject.dto.FriendShipDto;

import java.util.List;

public interface RelationshipMapper {

    void appendTargetToJson(AppendTargetToJsonDto appendTargetToJsonDto);

    List<String> getJsonKeysList(int memNo);

    void addFriendFromMember(FriendShipDto friendShipDto);

    void addFriendToMember(FriendShipDto friendShipDto);
}
