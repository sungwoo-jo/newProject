package com.sw.newProject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.newProject.dto.*;

import com.sw.newProject.enumType.NotificationType;
import com.sw.newProject.exception.CustomException;
import com.sw.newProject.service.FriendShipService;
import com.sw.newProject.service.MemberService;
import com.sw.newProject.service.NotificationService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.sw.newProject.enumType.ErrorCode.ALREADY_FOLLOWED;

@OpenAPIDefinition(info = @Info(title = "newProject API 명세서",
        description = "API 명세서",
        version = "v1",
        contact = @Contact(name = "sungwoo-jo", email = "sungwoo9671@naver.com")
)
)

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("member")
public class MemberController {
    private final MemberService memberService;
    private final NotificationService notificationService;
    private final FriendShipService friendShipService;

    @GetMapping("/join") // 회원가입 페이지 호출
    @Operation(summary = "회원가입 페이지 호출", description = "회원가입 페이지를 호출합니다.")
    public String getJoinPage() {
        return "/member/join";
    }

    @PostMapping("/doJoin") // 회원가입 처리
    @Operation(summary = "회원가입 처리", description = "실제 회원가입 처리를 합니다.")
    public String insertMember(MemberDto memberDto, @RequestParam("profileImage") MultipartFile file) throws Exception { // 회원가입 처리 후 boolean 으로 가입 여부 반환(0: 실패, 1: 성공)
        log.info("profileImageName fileInfo: {}", file);

        log.info("현재 작업경로: {}", System.getProperty("user.dir")); //현재 작업경로

        memberService.insertMember(memberDto, file);

        System.out.println("doJoin 호출");
        System.out.println("memberDto: " + memberDto);
        System.out.println("doJoin memPw: " + memberDto.getMemPw());
        memberDto.setDeleteYn(false);
        memberDto.setMemPw(memberService.passwordEncrypt(memberDto.getMemPw()));

        return "/member/joinSuccess";
    }

    @GetMapping("/joinSuccess")
    @Operation(summary = "회원가입 성공 페이지 호출", description = "회원가입 성공 페이지를 호출합니다.")
    public String getJoinSuccessPage() {
        return "/member/joinSuccess";
    }

    @GetMapping("/memberList") // 전체 회원 리스트 가져오기
    @Operation(summary = "전체 회원 리스트 조회", description = "전체 회원 리스트를 조회합니다.")
    public String getAllMember(Model model) {
        List<MemberDto> members = memberService.getAllMember();
        model.addAttribute("members", members);
        return "/member/memberList"; // memberList.html
    }

    @PatchMapping("/doUpdate") // 정보수정 처리
    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정 처리를 합니다.")
    public String updateMember(@RequestBody MemberDto memberDto, HttpSession session) throws NoSuchAlgorithmException {
        memberService.updateMember(memberDto);
        MemberDto updatedMemberDto = memberService.getMember(memberDto.getMemNo());
        session.setAttribute("member", updatedMemberDto); // 최신 정보로 세팅
        return "/member/joinSuccess"; // todo: 마이페이지 메인으로 이동해야 함
    }

    @GetMapping("/delete") // 회원 탈퇴 페이지 호출
    @Operation(summary = "회원 탈퇴 페이지 호출", description = "회원 탈퇴 페이지를 호출합니다.")
    public String getDeletePage() {
        return "/member/delete";
    }

    @PostMapping("/doDelete") // 회원탈퇴 처리
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 처리를 합니다.")
    @ResponseBody
    public String deleteMember(@RequestBody MemberDto memberDto, HttpSession session) {
        Integer memNo = memberDto.getMemNo();
        System.out.println("memNo: " + memNo);

        try {
            if (memNo != null && memNo != 0) {
                memberService.deleteMember(memNo);
                session.removeAttribute("member");
                return "Success";
            }
            return "Fail";
        } catch (Exception e) {
            return "Fail";
        }
    }

    @GetMapping("/duplicationIdCheck") // 중복 ID 검증
    @Operation(summary = "중복 ID 검증", description = "중복된 아이디가 있는 지 검증합니다.")
    @ResponseBody
    public ResponseEntity<Long> duplicationIdCheck(@RequestParam String memId) {
        System.out.println("[memberController][duplicationIdCheck][memId]: " + memId);
        Long count = memberService.duplicationIdCheck(memId);

        return ResponseEntity.ok(count);
    }

    @GetMapping("/duplicationNickNmCheck") // 중복 닉네임 검증
    @Operation(summary = "중복 닉네임 검증", description = "중복된 닉네임이 있는 지 검증합니다.")
    @ResponseBody
    public ResponseEntity<Long> duplicationNickNmCheck(@RequestParam String nickNm) {
        System.out.println("[memberController][duplicationNickNmCheck][nickNm]: " + nickNm);
        Long count = memberService.duplicationNickNmCheck(nickNm);

        return ResponseEntity.ok(count);
    }

    @GetMapping("/duplicationEmailCheck") // 중복 이메일 검증
    @Operation(summary = "중복 이메일 검증", description = "중복된 이메일이 있는 지 검증합니다.")
    @ResponseBody
    public ResponseEntity<Long> duplicationEmailCheck(@RequestParam String email) {
        System.out.println("[memberController][duplicationEmailCheck][email]: " + email);
        Long count = memberService.duplicationEmailCheck(email);

        return ResponseEntity.ok(count);
    }

    @GetMapping("/login") // 로그인 페이지 호출
    @Operation(summary = "로그인 페이지 호출", description = "로그인 페이지를 호출합니다.")
    public String getLoginPage() {
        return "/member/login";
    }

    @PostMapping("/doLogin") // 로그인 처리
    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    public ResponseEntity<String> doLogin(@RequestBody MemberDto memberDto, HttpSession session) throws NoSuchAlgorithmException, JsonProcessingException {
        MemberDto member = memberService.doLogin(memberDto); // 기본 데이터 가져오기

        if (member != null && member.getDeleteYn() != Boolean.TRUE) { // 로그인 성공
            // 세션 값 설정
            session.setAttribute("member", member);
            notificationService.subscribe(member.getMemNo()); // 로그인 성공 시 알림 구독
            log.info("member: {}", member);

            // 친구 리스트 불러와서 알림 전송
            String friendList = friendShipService.getFriendList(member.getMemNo());
            ObjectMapper objectMapper = new ObjectMapper();
            Map<Integer, String> map = objectMapper.readValue(friendList, new TypeReference<Map<Integer, String>>() {});
            for (Integer friend : map.keySet())
            {
                Executors.newSingleThreadExecutor().submit(() -> { // 알림 보내기 시작
//                    notificationService.notifyOne(friend, member.getMemNm() + "님이 로그인하셨습니다.");
                    log.info("로그인 알림 전송 완료");
                });
            }
            return ResponseEntity.ok("success");
        } else { // 로그인 실패
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/logout") // 로그아웃 처리
    @Operation(summary = "로그아웃 처리", description = "로그아웃을 진행합니다.")
    public String doLogout(HttpSession session) {
        session.removeAttribute("member");
        log.info("로그아웃 진행 -> 세션 삭제 완료");
        return "redirect:/";
    }

    @GetMapping("/findId") // 아이디 찾기 페이지 호출
    @Operation(summary = "아이디 찾기 페이지 호출", description = "아이디 찾기 페이지를 호출합니다.")
    public String findId() {
        return "/member/findId";
    }

    @PostMapping("/doFindId") // 아이디 찾기 처리
    @Operation(summary = "아이디 찾기", description = "아이디 찾기를 진행합니다")
    public ResponseEntity<String> doFindId(@RequestBody MemberDto memberDto) throws MessagingException, ExecutionException, InterruptedException, NotFoundException {
        if (memberService.findId(memberDto.getMemNm(), memberDto.getEmail()) == null) {
            log.info("member fot nound");
            throw new NotFoundException("입력하신 회원 정보를 다시 한 번 확인해주세요.");
        }
        Future<String> result = memberService.sendMailFindId(memberDto);
        return ResponseEntity.ok("sendMailSuccess");
    }

    @GetMapping("/findPw") // 비밀번호 찾기 페이지 호출
    @Operation(summary = "비밀번호 찾기 페이지 호출", description = "비밀번호 찾기 페이지를 호출합니다.")
    public String findPw() {
        return "/member/findPw";
    }

    @PostMapping("doFindPw") // 비밀번호 찾기 처리(회원 정보 일치 여부 확인)
    @Operation(summary = "비밀번호 찾기", description = "비밀번호 찾기를 진행합니다.")
    public ResponseEntity<String> doFindPw(@RequestBody MemberDto memberDto) throws NoSuchAlgorithmException, NotFoundException {
        String memNm = memberDto.getMemNm();
        String email = memberDto.getEmail();
        String memId = memberDto.getMemId();
        if (memberService.findPw(memNm, email, memId) == null) {
            log.info("member fot nound");
            throw new NotFoundException("입력하신 회원 정보를 다시 한 번 확인해주세요.");
        }
        Future<String> result = memberService.sendMailFindPw(memberDto);
        return ResponseEntity.ok("sendMailSuccess");
    }

    @GetMapping("resetPw") // 임시 비밀번호 발송 완료 페이지 호출
    @Operation(summary = "임시 비밀번호 발송 완료 페이지 호출", description = "임시 비밀번호 발송 완료 페이지를 호출합니다.")
    public String resetPw() {
        return "/member/resetPw";
    }

    @PatchMapping("doResetPw") // 비밀번호 재설정 처리
    @Operation(summary = "비밀번호 재설정", description = "비밀번호를 재설정 처리를 진행합니다.")
    public ResponseEntity<String> doResetPw(@RequestBody DoResetPwDto doResetPwDto) throws NoSuchAlgorithmException {
        System.out.println("[MemberController][doResetPw][doResetPwDto]: " + doResetPwDto);
        memberService.doResetPw(doResetPwDto); // 회원 번호, 새롭게 설정할 비밀번호
        return ResponseEntity.ok("resetPwSuccess");
    }

    @GetMapping("resetPwSuccess") // 비밀번호 재설정 완료 페이지 호출
    @Operation(summary = "비밀번호 재설정 완료 페이지 호출", description = "비밀번호 재설정 완료 페이지를 호출합니다.")
    public String resetPwSuccess() {
        return "/member/resetPwSuccess";
    }

    /*
     * 팔로우 처리하는 메서드
     * 해당 메서드 내에서 팔로우와 팔로잉을 동시에 처리한다.
     * 팔로우: 내가 다른 사람을 따라가는 것, 팔로잉: 다른 사람이 나를 따라오는 것
     */
    @PostMapping("/follow") // 팔로우 처리
    @Operation(summary = "팔로우 처리", description = "팔로우 처리를 진행합니다.")
    public ResponseEntity<String> doFollow(@RequestBody BoardDto boardDto, HttpSession session) {
        log.info("팔로우 시도: {}", session.getAttribute("member"));
        log.info("대상 게시글: {}", boardDto);
        MemberDto memberDto = (MemberDto)session.getAttribute("member");
        log.info("memberDto: {}", memberDto);


        HashMap<String, String> followData = new HashMap<>(); // 팔로우데이터 담는 맵

        Date now = new Date();
        SimpleDateFormat followDt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        HashMap<String, String> recentFollowData = memberService.getFollowData(memberDto.getMemNo()); // 회원의 현재 팔로우 데이터를 가져오기
        JSONObject recentFollowDataJson = new JSONObject(recentFollowData);

        NotificationDto notificationDto = new NotificationDto();

        if (recentFollowData == null) { // 팔로우 데이터가 없는 경우
            recentFollowData = new HashMap<>(); // 새롭게 초기화
            recentFollowData.put(String.valueOf(boardDto.getMemNo()), followDt.format(now)); // 팔로우하는 유저의 데이터를 put
            JSONObject newRecentFollowData = new JSONObject(recentFollowData); // json으로 변환

            // 최종적으로 팔로워 데이터 담아주기
            followData.put("memNo", String.valueOf(memberDto.getMemNo()));
            followData.put("followData", String.valueOf(newRecentFollowData));
        } else { // 팔로우 데이터 있으면 현재 데이터에다가 추가만 해주면 됨
            // "follow" 값을 가져와서 이스케이프 처리된 JSON 문자열을 얻음
            String followDataJson = recentFollowDataJson.getString("follow");
            JSONObject prevFollowData = new JSONObject(followDataJson);

            if (prevFollowData.has(boardDto.getMemNo().toString())) { // 이미 팔로우된 회원이라면 Exception 발생
                throw new CustomException(ALREADY_FOLLOWED, "이미 팔로우 된 회원입니다.");
            } else {
                // 두 번째로, 이스케이프 처리된 JSON 문자열을 다시 JSONObject로 변환
                JSONObject newRecentFollowData = new JSONObject(followDataJson);
                newRecentFollowData.put(String.valueOf(boardDto.getMemNo()), followDt.format(now));
                log.info("newRecentFollowData: {}", newRecentFollowData);

                // 최종적으로 팔로우 데이터 담아주기
                followData.put("memNo", String.valueOf(memberDto.getMemNo()));
                followData.put("followData", String.valueOf(newRecentFollowData));
            }
        }

        memberService.insertFollowData(followData); // 팔로우 데이터 insert

        HashMap<String, String> followerData = new HashMap<>(); // 팔로워데이터 담는 맵


        HashMap<String, String> recentFollwingData = memberService.getFollowingData(boardDto.getMemNo()); // 회원의 현재 팔로워 데이터를 가져오기
            if (recentFollwingData == null) { // 팔로워 데이터가 없는 경우
                recentFollwingData = new HashMap<>(); // 새롭게 초기화
                recentFollwingData.put(String.valueOf(memberDto.getMemNo()), followDt.format(now)); // 팔로잉하는 유저의 데이터에 팔로우 유저의 데이터를 추가
                JSONObject newRecentFollowingData = new JSONObject(recentFollwingData);

                // 최종적으로 팔로워 데이터 담아주기
                followerData.put("memNo", String.valueOf(boardDto.getMemNo()));
                followerData.put("followingData", String.valueOf(newRecentFollowingData));

                log.info("followerData: {}", followerData);
                memberService.insertFollowingData(followerData);
            }
            else { // 팔로잉 데이터가 있는 경우(데이터를 가져와서 추가해주고 followerData에 넣으면 됨)
                JSONObject recentFollowingDataJson = new JSONObject(recentFollwingData);
                String followingDataJson = recentFollowingDataJson.getString("following");
                JSONObject prevFollowingData = new JSONObject(followingDataJson);
                // 현재 팔로잉 데이터를 정제해 가져오기
                prevFollowingData.put(String.valueOf(memberDto.getMemNo()), followDt.format(now));
                followerData.put("memNo", String.valueOf(boardDto.getMemNo()));
                followerData.put("followingData", String.valueOf(prevFollowingData));

                // 최종적으로 팔로워 데이터 담아주기
                memberService.insertFollowData(followData);
                memberService.insertFollowingData(followerData);

                // 팔로잉 알림 전송
                // 작성자에게 알림 전송
                notificationDto.setToMemNo(boardDto.getMemNo());
                notificationDto.setFromMemNo(memberDto.getMemNo());
                notificationDto.setContent(memberDto.getMemId() + "님이 팔로우 하였습니다.");
                notificationDto.setUrl("/mypage");
                notificationDto.setNotificationType(NotificationType.FOLLOW_ADD);
                notificationService.notifyOne(notificationDto.getToMemNo(), notificationDto.getContent(), notificationDto.getNotificationType());
            }

        return ResponseEntity.ok("success");
    }

    /*
     * 팔로우 취소 처리하는 메서드
     * 해당 메서드 내에서 이미 팔로우된 정보를 삭제한다.
     */
    @DeleteMapping("/cancelFollow") // 팔로우 취소 처리
    @Operation(summary = "팔로우 취소", description = "팔로우 취소 처리를 진행합니다.")
    public ResponseEntity<String> doCancelFollow(@RequestBody BoardDto boardDto, HttpSession session) {
        HashMap<String, Object> map = new HashMap<>();
        MemberDto memberDto = (MemberDto) session.getAttribute("member");
        Integer memberDtoNo = memberDto.getMemNo(); // 로그인한 사용자
        Integer boardDtoNo = boardDto.getMemNo(); // 게시글 작성자
        map.put("memberDtoNo", memberDtoNo);
        map.put("boardDtoNo", boardDtoNo);

        memberService.doCancelFollow(map);
        memberService.doCancelFollowing(map);

        return ResponseEntity.ok("success");
    }
}

