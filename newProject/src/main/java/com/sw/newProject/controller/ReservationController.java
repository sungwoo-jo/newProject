package com.sw.newProject.controller;

import com.sw.newProject.dto.ReservationDto;
import com.sw.newProject.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ReservationController {
    private final ReservationService reservationService;

        /*
         * 예약을 조회하는 메서드
         * 회원번호에 해당하는 예약을 모두 조회한다.
         */
    public List<ReservationDto> getReservationById(Integer memNo, Model model) {
        return reservationService.getReservationById(memNo);
    }

    /*
     * 예약을 생성하는 메서드
     * 아직 userType이 ADMIN인 경우에 처리하는 내용은 구분하지 못했음
     */
    public ReservationDto createReservation(ReservationDto reservationDto, Model model) {
        return reservationService.createReservation(reservationDto);
    }

    /*
     * 예약을 취소하는 메서드
     * 예약 취소 시 취소 정보는 사라지지 않으며 추후 전체 예약 리스트에 모든 상태의 예약건이 보이게 된다.
     */
    public String cancelReservation(ReservationDto reservationDto) {
        return reservationService.cancelReservation(reservationDto);
    }

    /*
     * 예약 상세보기 페이지 메서드
     * 여러 예약건 중 하나만 조회한다.
     */
    public ReservationDto viewReservationDto(ReservationDto reservationDto) {
        return reservationService.viewReservationDto(reservationDto);
    }
}
