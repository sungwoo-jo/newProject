package com.sw.newProject.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDto extends BaseTimeEntity{

    private Integer postNo; // 쪽지번호
    private String senderMemId; // 발신회원아이디
    private Integer senderMemNo; // 발신회원번호
    private String receiverMemId; // 수신회원아이디
    private Integer receiverMemNo; // 수신회원번호
    private String subject; // 제목
    private String contents; // 내용
    private Boolean readYn; // 읽음여부
    private Boolean deleteBySender; // 발신회원 삭제여부
    private Boolean deleteByReceiver; // 수신회원 삭제여부
    private LocalDateTime sendDt; // 전송일시
    private LocalDateTime readDt; // 확인일시
}
