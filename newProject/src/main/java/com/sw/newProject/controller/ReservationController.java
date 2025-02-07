package com.sw.newProject.controller;

import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.dto.ReservationDto;
import com.sw.newProject.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;
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

    /*
     * 예약을 생성하는 메서드
     * 아직 userType이 ADMIN인 경우에 처리하는 내용은 구분하지 못했음
     */
    @PostMapping("/create")
    public void createReservation(@RequestBody ReservationDto reservationDto) {
        log.info("reservationDto: " + reservationDto);
        int result = reservationService.createReservation(reservationDto);
    }

    /*
     * 예약을 취소하는 메서드
     * 예약 취소 시 취소 정보는 사라지지 않으며 추후 전체 예약 리스트에 모든 상태의 예약건이 보이게 된다.
     */
    @DeleteMapping("/cancel")
    public void cancelReservation(ReservationDto reservationDto) {
        reservationService.cancelReservation(reservationDto);
    }

    /*
     * 예약 상세보기 페이지 메서드
     * 여러 예약건 중 하나만 조회한다.
     */
    @GetMapping("/view")
    @ResponseBody
    public ReservationDto viewReservationDto(ReservationDto reservationDto) {
        return reservationService.viewReservationDto(reservationDto);
    }
}
