package com.sw.newProject.dto;

import com.sw.newProject.enumType.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private int sno; // 번호

    private int toMemNo;  // 보낸사람 회원 번호

    private int fromMemNo; // 받는사람 회원 번호

    private NotificationType notificationType; // 알림 종류별 분류

    private Boolean readYn; // 알림 확인 여부

    private Boolean deleteYn; // 알림 확인 여부

    private String content; // 알림의 내용

    private String url; // 알림 클릭 시 이동할 url

    private LocalDateTime regDt; // 보낸 일시

    private LocalDateTime modDt; // 수정 일시
}
