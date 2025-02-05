package com.sw.newProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {

    public enum UserType {
        MEMBER,
        ADMIN
    }

    public enum RsvStatus {
        PENDING,
        CONFIRMED,
        CANCELED
    }

    /*
     * 예약 관련 기본 Dto
     * 예약 시 필요한 항목들을 멤버변수로 가지고 있다.
     */
    private Long rsvId; // 예약번호

    private UserType userType; // 사용자 유형(MEMBER, ADMIN)

    private Integer memNo; // 회원번호

    private Long placeId; // 장소번호

    private LocalDateTime rsvTime; // 예약시간

    private RsvStatus rsvStatus; // 예약상태

    private String reqMsg; // 요청사항

    private Boolean deleteYn; // 삭제여부
}
