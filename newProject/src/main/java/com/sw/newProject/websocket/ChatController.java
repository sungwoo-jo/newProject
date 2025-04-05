package com.sw.newProject.websocket;

import com.sw.newProject.dto.ChatEntrantDto;
import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.dto.SaveEntrantDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @RequestMapping("/chat/chatList")
    public String chatList(Model model, HttpSession session){
        MemberDto memberDto = (MemberDto) session.getAttribute("member");
        List<ChatRoom> roomList = chatService.findAcceptRoom(memberDto.getMemNo());
        for (ChatRoom chatRoom : roomList) {
            chatService.createRoom(chatRoom.getRoomNm(), chatRoom.getRoomId());
        }

        for (ChatRoom chatRoom : roomList) {
            log.info("chatRoomInfo[getRoomNm]: {}, [getRoomId]: {}", chatRoom.getRoomNm(), chatRoom.getRoomId());
        }

        model.addAttribute("roomList",roomList);
        return "chat/chatList";
    }


    @PostMapping("/chat/createRoom")  //방을 만들었으면 해당 방으로 가야지.
    public String createRoom(Model model, @RequestParam String name, HttpSession session) {
        HashMap<String, Object> map = new HashMap<>();
        ChatRoom room = chatService.createRoom(name);
        MemberDto memberDto = (MemberDto)session.getAttribute("member");
        String username = memberDto.getMemNm();
        ChatEntrantDto entrant = new ChatEntrantDto();

        model.addAttribute("room",room);
        model.addAttribute("username",username);

        map.put("roomNm", room.getRoomNm());
        map.put("roomId", room.getRoomId());
        map.put("maker", memberDto.getMemNo()); // 채팅방 생성자

        // 채팅방 정보 insert
        chatService.saveRoomInfo(map);
        entrant.setSno(Integer.parseInt(String.valueOf(map.get("sno")))); // 생성 시 저장된 채팅방 번호
        entrant.setEntrant(memberDto.getMemNo()); // 생성자는 항상 들어가야 하므로
        entrant.acceptRequest(); // 바로 수락 처리
        chatService.saveEntrant(entrant);

        return "chat/chatRoom";
    }

    @GetMapping("/chat/chatRoom")
    public String chatRoom(Model model, @RequestParam String roomId, HttpSession session){
        log.info("roomId: {}", roomId);
        ChatRoom room = chatService.findRoomById(roomId);
        log.info("room: {}", room);
        MemberDto memberDto = (MemberDto)session.getAttribute("member");
        String username = memberDto.getMemNm();
        model.addAttribute("room",room);   //현재 방에 들어오기위해서 필요한데...... 접속자 수 등등은 실시간으로 보여줘야 돼서 여기서는 못함
        model.addAttribute("username",username);
        return "chat/chatRoom";
    }
}
