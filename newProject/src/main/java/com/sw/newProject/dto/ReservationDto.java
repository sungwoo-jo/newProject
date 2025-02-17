package com.sw.newProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    /*
     * 예약 관련 기본 Dto
     * 예약 시 필요한 항목들을 멤버변수로 가지고 있다.
     */
    private enum UserType {
        USER, ADMIN
    }

    private UserType userType; // 유저타입(사용자 | 어드민)

    private Long rsvNo; // 예약번호

    private Integer memNo; // 회원번호 (todo: 추후 어드민도 회원번호를 하나 만들자 mgrNo 로)

    private Long placeId; // 장소번호

    private LocalDateTime rsvDt; // 예약일

    private String rsvStatus; // 예약상태

    private String reqMsg; // 요청사항

    private Boolean deleteYn; // 삭제여부


}
