package com.sw.newProject.mapper;

import com.sw.newProject.dto.ReservationDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationMapper {

    List<ReservationDto> getReservationById(Integer memNo);

    ReservationDto createReservation(ReservationDto reservationDto);

    String cancelReservation(ReservationDto reservationDto);

    ReservationDto viewReservation(ReservationDto reservationDto);
}
