package com.sw.newProject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sw.newProject.dto.ChatEntrantDto;
import com.sw.newProject.dto.FriendShipDto;
import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.dto.NotificationDto;
import com.sw.newProject.enumType.ChatEntrantStatus;
import com.sw.newProject.service.FriendShipService;
import com.sw.newProject.service.MemberService;
import com.sw.newProject.service.NotificationService;
import com.sw.newProject.websocket.ChatRoom;
import com.sw.newProject.websocket.ChatService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@OpenAPIDefinition(info = @Info(title = "newProject API 명세서",
        description = "API 명세서",
        version = "v1",
        contact = @Contact(name = "sungwoo-jo", email = "sungwoo9671@naver.com")
)
)

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    private final MemberService memberService;
    private final FriendShipService friendShipService;
    private final ChatService chatService;
    private final NotificationService notificationService;

    @GetMapping(value = {"/", "/index", ""})
    public String getIndexPage(HttpSession session) {
        MemberDto memberDto = (MemberDto) session.getAttribute("member");
        String friendList = friendShipService.getFriendList(memberDto.getMemNo());

        List<MemberDto> friends = new ArrayList<>();

        // 1. 친구 목록 가져오기(3명)
        // JSON 문자열을 JSONObject로 파싱
        JSONObject friendListJson = new JSONObject(friendList);
        log.info("friendListJson: {}", friendListJson);

        // 친구 목록 순회(메인에 보여주는 것이기 때문에 3개만)
        int count = 0;
        for (String friendId : friendListJson.keySet()) {

            MemberDto friendMemberDto = memberService.getMember(Integer.parseInt(friendId));

            friends.add(friendMemberDto);
            count++;
            if (count >= 3) break;
        }

        log.info("friends: {}", friends);

        // 2. 최근 알림 가져오기(3개)
        ArrayList<NotificationDto> recentNotifications = notificationService.getRecentNotification(memberDto.getMemNo());
        for (NotificationDto dto : recentNotifications) {
            log.info("contents: {}", dto.getContent());
        }


        // 3. 팔로잉 소식 가져오기

        // 4. 팔로워 소식 가져오기


        session.setAttribute("friends", friends);
        session.setAttribute("recentNotifications", recentNotifications);

        return "mypage/index";
    }

    @GetMapping("/update")
    public String update() { // 정보 수정 페이지 호출
        return "mypage/update";
    }

    @PostMapping("/doUpdate")
    public String doUpdate(MemberDto memberDto, HttpSession session) throws NoSuchAlgorithmException {
        log.info("memberDto: {}", memberDto);
        memberService.updateMember(memberDto);
        MemberDto updatedMemberDto = memberService.getMember(memberDto.getMemNo());
        session.setAttribute("member", updatedMemberDto);
        return "mypage/index";
    }

    @GetMapping("/follow/list")
    public String getFollowList(Model model) {
        
        return "mypage/followList";
    }

    @GetMapping("/following/list")
    public String getFollowingList(Model model) {
        return "mypage/followingList";
    }

    @GetMapping("/friendList")
    public String getFriendList(HttpSession session, Model model) {
        // 1. 친구 목록 조회
        MemberDto memberDto = (MemberDto) session.getAttribute("member");
        String friendList = friendShipService.getFriendList(memberDto.getMemNo());

        List<MemberDto> friendLists = new ArrayList<>();

        // JSON 문자열을 JSONObject로 파싱
        JSONObject friendListJson = new JSONObject(friendList);

        log.info("friendListJson: {}", friendListJson);

        for (String friendId : friendListJson.keySet()) {
            String timestamp = friendListJson.getString(friendId);
            MemberDto friendMemberDto = memberService.getMember(Integer.parseInt(friendId));

            friendLists.add(friendMemberDto);
        }

        model.addAttribute("friendLists", friendLists); // 친구 목록 리턴

        // 2. 받은 친구 요청 조회(fromMemNo가 자신이고, status가 REQUEST)
        List<MemberDto> recentFriendLists = friendShipService.getReceivedRequest(memberDto.getMemNo());
        log.info("recentFriendLists: {}", recentFriendLists);
        model.addAttribute("recentFriendLists", recentFriendLists); // 받은 친구 요청 리턴

        // 3. 보낸 친구 요청 조회(toMemNo가 자신이고, status가 REQUEST)
        List<MemberDto> requestFriendLists = friendShipService.getSentRequest(memberDto.getMemNo());
        log.info("requestFriendLists: {}", requestFriendLists);
        model.addAttribute("requestFriendLists", requestFriendLists); // 받은 친구 요청 리턴

        return "mypage/friendList";
    }

    @PostMapping("/inviteChat")
    public String inviteChat(@RequestParam(name = "friendNo") List<Integer> friendNo, HttpSession session) {
        HashMap<String, Object> map = new HashMap<>();
        String chatRoomName = "";
        if (friendNo.size() > 1) {
             chatRoomName = memberService.getMember(friendNo.get(0)).getMemNm() + " 외 " + (friendNo.size() - 1) + "명";
        } else {
            chatRoomName = memberService.getMember(friendNo.get(0)).getMemNm();
        }
        ChatRoom room = chatService.createRoom(chatRoomName);
        MemberDto memberDto = (MemberDto)session.getAttribute("member");
        ChatEntrantDto entrant = new ChatEntrantDto();

        map.put("roomNm", room.getRoomNm());
        map.put("roomId", room.getRoomId());
        map.put("maker", memberDto.getMemNo()); // 채팅방 생성자

        chatService.saveRoomInfo(map); // 채팅방 생성

        // 생성자의 참여자 데이터 insert
        entrant.setSno(Integer.parseInt(String.valueOf(map.get("sno")))); // 생성 시 저장된 채팅방 번호
        entrant.setEntrant(memberDto.getMemNo()); // 생성자는 항상 들어가야 하므로
        entrant.acceptRequest(); // 바로 수락 처리
        chatService.saveEntrant(entrant);

        // 초대받은 사람들의 데이터 insert
        for (Integer friend : friendNo) {
            entrant.setSno(Integer.parseInt(String.valueOf(map.get("sno")))); // 생성 시 저장된 채팅방 번호
            entrant.setEntrant(friend);
            entrant.setStatus(ChatEntrantStatus.REQUEST);
            chatService.saveEntrant(entrant);
        }

        return "success";
    }

    @PatchMapping("/acceptFriend")
    public ResponseEntity<String> acceptFriend(@RequestBody List<Integer> friendNo, HttpSession session) throws JsonProcessingException {
        MemberDto memberDto = (MemberDto) session.getAttribute("member");
        FriendShipDto friendShipDto = new FriendShipDto();
        friendShipDto.setFromMemNo(memberDto.getMemNo());
        for (Integer friend : friendNo) {
            log.info("friend: {}", friend);
            friendShipDto.setToMemNo(friend);
            friendShipService.acceptRequest(friendShipDto);
        }

        return ResponseEntity.ok("success");
    }

    @PatchMapping("/rejectRequest")
    public ResponseEntity<String> rejectRequest(@RequestBody List<Integer> friendNo, HttpSession session) {
        MemberDto memberDto = (MemberDto) session.getAttribute("member");
        FriendShipDto friendShipDto = new FriendShipDto();
        friendShipDto.setFromMemNo(memberDto.getMemNo());
        for (Integer friend : friendNo) {
            log.info("friend: {}", friend);
            friendShipDto.setToMemNo(friend);
            friendShipService.rejectRequest(friendShipDto);
        }

        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/cancelRequest")
    public ResponseEntity<String> cancelRequest(@RequestBody List<Integer> friendNo, HttpSession session) {
        MemberDto memberDto = (MemberDto) session.getAttribute("member");
        FriendShipDto friendShipDto = new FriendShipDto();
        friendShipDto.setToMemNo(memberDto.getMemNo());
        for (Integer friend : friendNo) {
            log.info("friend: {}", friend);
            friendShipDto.setFromMemNo(friend);
            friendShipService.cancelRequest(friendShipDto);
        }

        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/deleteFriend")
    public ResponseEntity<String> deleteFriend(@RequestBody List<Integer> friendNo, HttpSession session) {
        MemberDto memberDto = (MemberDto) session.getAttribute("member");
        FriendShipDto friendShipDto = new FriendShipDto();
        friendShipDto.setToMemNo(memberDto.getMemNo());

        for (Integer friend : friendNo) {
            log.info("friend: {}", friend);
            friendShipDto.setFromMemNo(friend);
            friendShipService.cancelRequest(friendShipDto);
            friendShipService.deleteFriend(friendShipDto);
        }

        return ResponseEntity.ok("success");
    }
}
