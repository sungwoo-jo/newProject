package com.sw.newProject.websocket;

import com.sw.newProject.dto.MemberDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;


    @RequestMapping("/chat/chatList")
    public String chatList(Model model){
        List<ChatRoom> roomList = chatService.findAllRoom();
        model.addAttribute("roomList",roomList);
        return "chat/chatList";
    }


    @PostMapping("/chat/createRoom")  //방을 만들었으면 해당 방으로 가야지.
    public String createRoom(Model model, @RequestParam String name, HttpSession session) {
        ChatRoom room = chatService.createRoom(name);
        MemberDto memberDto = (MemberDto)session.getAttribute("member");
        String username = memberDto.getMemNm();
        model.addAttribute("room",room);
        model.addAttribute("username",username);
        return "chat/chatRoom";  //만든사람이 채팅방 1빠로 들어가게 됩니다
    }

    @GetMapping("/chat/chatRoom")
    public String chatRoom(Model model, @RequestParam String roomId, HttpSession session){
        ChatRoom room = chatService.findRoomById(roomId);
        MemberDto memberDto = (MemberDto)session.getAttribute("member");
        String username = memberDto.getMemNm();
        model.addAttribute("room",room);   //현재 방에 들어오기위해서 필요한데...... 접속자 수 등등은 실시간으로 보여줘야 돼서 여기서는 못함
        model.addAttribute("username",username);
        return "chat/chatRoom";
    }
}
