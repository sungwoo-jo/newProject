package com.sw.newProject.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.newProject.dto.SaveEntrantDto;
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
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }

    public void saveRoomInfo(HashMap<String, Object> map) {
        chatMapper.saveRoomInfo(map);
    }

    public void saveEntrant(SaveEntrantDto saveEntrantDto) {
        chatMapper.saveEntrant(saveEntrantDto);
    }
}
