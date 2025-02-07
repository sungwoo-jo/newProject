package com.sw.newProject.service;

import com.sw.newProject.dto.ReservationDto;
import com.sw.newProject.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationMapper reservationMapper;

    public List<ReservationDto> getReservationById(Integer memNo) {
        List<ReservationDto> reservationDto = reservationMapper.getReservationById(memNo);
        for (ReservationDto res : reservationDto) { // 예약 확인 페이지에 노출을 위해 예약 상태를 한글로 치환
            switch (res.getRsvStatus()) {
                case "WAIT" -> res.setRsvStatus("예약대기");
                case "PENDING" -> res.setRsvStatus("예약승인대기");
                case "CONFIRMED" -> res.setRsvStatus("예약승인");
                case "CANCELED" -> res.setRsvStatus("예약취소");
            }
        }
        return reservationDto;
    }

    public int createReservation(ReservationDto reservationDto) {
        return reservationMapper.createReservation(reservationDto);
    }

    public void cancelReservation(ReservationDto reservationDto) {
        reservationMapper.cancelReservation(reservationDto);
    }

    public ReservationDto viewReservationDto(ReservationDto reservationDto) {
        return reservationMapper.viewReservation(reservationDto);
    }
}
