package com.sw.newProject.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDto {

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
    private LocalDateTime regDt; // 생성일시
    private LocalDateTime modDt; // 수정일시
}
