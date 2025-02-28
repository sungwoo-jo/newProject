package com.sw.newProject.controller;

import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.dto.ReservationDto;
import com.sw.newProject.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/reservation")
@RequiredArgsConstructor
@Controller
public class ReservationController {
    private final ReservationService reservationService;

        /*
         * 예약을 조회하는 메서드
         * 회원번호에 해당하는 예약을 모두 조회한다.
         */
    @GetMapping("/list")
    public String getReservationById(Model model, @SessionAttribute("member") MemberDto member) {
        List<ReservationDto> reservationDto = reservationService.getReservationById(member.getMemNo()); // 로그인한 회원의 memNo로 조회
        model.addAttribute("reservationDto", reservationDto);

        return "/reservation/list";
    }

    @GetMapping("/new")
    public String getReservationPage() {
        return "/reservation/new";
    }

    /*
     * 예약을 생성하는 메서드
     * 아직 userType이 ADMIN인 경우에 처리하는 내용은 구분하지 못했음
     */
    @PostMapping("/create")
    public ResponseEntity<String> createReservation(@RequestBody ReservationDto reservationDto, HttpSession session) {
        MemberDto member = (MemberDto) session.getAttribute("member");
        if (member != null) { // 회원으로 로그인 한 상태라면 회원번호를 함께 전달하고, 비회원이면 기본적으로 0으로 세팅된다.
            Integer memNo = member.getMemNo();
            log.info("memNo: {}", memNo);
            reservationDto.setMemNo(memNo);
        }
        reservationDto.setPlaceId(11111L); // 테스트를 위해 우선 placeId는 임의로 세팅
        reservationDto.setRsvStatus("PENDING"); // 테스트를 위해 우선 rsvStatus는 임의로 세팅
        log.info("reservationDto: " + reservationDto);
        int result = reservationService.createReservation(reservationDto);
        return result > 0 ? ResponseEntity.ok("success") : ResponseEntity.ok("fail");
    }

    /*
     * 예약을 취소하는 메서드
     * 예약 취소 시 취소 정보는 사라지지 않으며 예약 리스트에는 예약 취소 건까지 함께 보이게 된다.
     */
    @DeleteMapping("/cancel/{rsvNo}")
    public ResponseEntity<String> cancelReservation(@PathVariable Integer rsvNo) {
        int result = reservationService.cancelReservation(rsvNo); //
        return result > 0 ? ResponseEntity.ok("success") : ResponseEntity.ok("fail");
    }

    /*
     * 예약 상세보기 페이지 메서드
     * 여러 예약건 중 하나만 조회한다.
     */
    @GetMapping("/view/{rsvNo}")
//    @ResponseBody
    public String viewReservation(@PathVariable Integer rsvNo, Model model) {
        ReservationDto result = reservationService.viewReservation(rsvNo);
        model.addAttribute("reservationDto", result);
        return "/reservation/view";
    }
}
