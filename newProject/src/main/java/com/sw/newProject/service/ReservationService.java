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
        return reservationMapper.getReservationById(memNo);
    }

    public ReservationDto createReservation(ReservationDto reservationDto) {
        return reservationMapper.createReservation(reservationDto);
    }

    public String cancelReservation(ReservationDto reservationDto) {
        return reservationMapper.cancelReservation(reservationDto);
    }

    public ReservationDto viewReservationDto(ReservationDto reservationDto) {
        return reservationMapper.viewReservation(reservationDto);
    }
}
