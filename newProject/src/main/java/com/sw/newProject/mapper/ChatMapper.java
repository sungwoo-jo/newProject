package com.sw.newProject.mapper;

import com.sw.newProject.dto.ChatEntrantDto;
import com.sw.newProject.dto.SaveEntrantDto;
import com.sw.newProject.websocket.ChatRoom;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ChatMapper {
    void saveRoomInfo(HashMap<String, Object> map);

    void saveEntrant(ChatEntrantDto entrant);

    List<ChatRoom> findAllRoom();

    List<ChatRoom> findAcceptRoom(int memNo);
}
