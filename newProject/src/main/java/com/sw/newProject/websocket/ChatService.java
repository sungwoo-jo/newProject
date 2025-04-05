package com.sw.newProject.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.newProject.dto.ChatEntrantDto;
import com.sw.newProject.mapper.ChatMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;
    private final ChatMapper chatMapper;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return chatMapper.findAllRoom();
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom(String roomNm) { // 새로운 채팅방 생성할때
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .roomNm(roomNm)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }

    public void createRoom(String roomNm, String roomId) { // 유저의 채팅방 리스트를 불러올 때 
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(roomId)
                .roomNm(roomNm)
                .build();
        chatRooms.put(roomId, chatRoom);
    }

    public void saveRoomInfo(HashMap<String, Object> map) {
        chatMapper.saveRoomInfo(map);
    }

    public void saveEntrant(ChatEntrantDto entrant) {
        chatMapper.saveEntrant(entrant);
    }

    public List<ChatRoom> findAcceptRoom(int memNo) {
        return chatMapper.findAcceptRoom(memNo);
    }
}
