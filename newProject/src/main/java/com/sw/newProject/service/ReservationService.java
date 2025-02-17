package com.sw.newProject.service;

import com.sw.newProject.dto.ReservationDto;
import com.sw.newProject.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationMapper reservationMapper;

    public List<ReservationDto> getReservationById(Integer memNo) {
        List<ReservationDto> reservationDto = reservationMapper.getReservationById(memNo);
        for (ReservationDto res : reservationDto) {
            replaceRsvStatus(res);
        }
        return reservationDto;
    }

    public int createReservation(ReservationDto reservationDto) {
        return reservationMapper.createReservation(reservationDto);
    }

    public int cancelReservation(Integer rsvNo) {
        return reservationMapper.cancelReservation(rsvNo);
    }

    public ReservationDto viewReservation(Integer rsvNo) {
        ReservationDto reservationDto = reservationMapper.viewReservation(rsvNo);
        replaceRsvStatus(reservationDto);
        return reservationDto;
    }

    public void replaceRsvStatus(ReservationDto rsv) { // 예약 확인 페이지에 노출을 위해 예약 상태를 한글로 치환하는 메서드
        switch (rsv.getRsvStatus()) {
            case "WAIT" -> rsv.setRsvStatus("예약대기");
            case "PENDING" -> rsv.setRsvStatus("예약승인대기");
            case "CONFIRMED" -> rsv.setRsvStatus("예약승인");
            case "CANCELED" -> rsv.setRsvStatus("예약취소");
        }
    }
}
