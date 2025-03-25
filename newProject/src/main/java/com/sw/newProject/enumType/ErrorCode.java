package com.sw.newProject.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 400
    UNKNOWN(HttpStatus.BAD_REQUEST, "000_UNKNOWN", "알 수 없는 에러가 발생했습니다."),
    ENCRYPTION_FAILED(HttpStatus.BAD_REQUEST, "001_ENCRYPTION_FAILED", "암호화에 실패하셨습니다."),
    DECRYPTION_FAILED(HttpStatus.BAD_REQUEST, "002_DECRYPTION_FAILED", "복호화에 실패하셨습니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "003_DUPLICATED_EMAIL", "이미 등록되어 있는 이메일입니다."),
    REGISTERED_EMAIL_FOR_THE_OTHER(HttpStatus.BAD_REQUEST, "004_REGISTERED_EMAIL_FOR_THE_OTHER", "다른 서비스로 등록되어 있는 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "005_INVALID_PASSWORD", "유효하지 않은 비밀번호 입니다."),
    NOT_EXISTED_EMAIL(HttpStatus.BAD_REQUEST, "006_NOT_EXISTED_EMAIL", "존재하지 않는 회원입니다."),
    BLOCKED_EMAIL(HttpStatus.BAD_REQUEST, "007_BLOCKED_EMAIL", "차단된 사용자 입니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "008_WRONG_PASSWORD", "틀린 비밀번호 입니다."),
    NOT_ALLOW_EMAIL(HttpStatus.BAD_REQUEST, "009_NOT_ALLOW_EMAIL", "이메일 사용이 허용이 되지 않은 사용자입니다."),
    ALREADY_FOLLOWED(HttpStatus.BAD_REQUEST, "010_ALREADY_FOLLOWED", "이미 팔로우 한 회원입니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "011_EXPIRED_TOKEN", "만료된 토큰입니다."),
    ALREADY_FRIEND_LIST(HttpStatus.BAD_REQUEST, "012_ALREADY_FRIEND_LIST", "이미 존재하는 친구 요청입니다."),
    IMPOSSIBLE_REQUEST(HttpStatus.BAD_REQUEST, "013_IMPOSSIBLE_REQUEST", "친구 수락이 불가한 상태입니다."),
    IMPOSSIBLE_REJECT(HttpStatus.BAD_REQUEST, "014_IMPOSSIBLE_REJECT", "거절이 불가한 상태입니다."),
    ALREADY_ACCEPT_FRIEND(HttpStatus.BAD_REQUEST, "015_ALREADY_ACCEPT_FRIEND", "이미 추가한 친구입니다."),
    NOT_EXISTED_REQUEST(HttpStatus.BAD_REQUEST, "016_NOT_EXISTED_REQUEST", "보낸 요청이 없습니다."),

    // 401
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "101_INVALID_TOKEN", "유효하지 않은 토큰입니다."),


    // 403
    ACESS_DENIED_EMAIL(HttpStatus.FORBIDDEN, "201_ACESS_DENIED_EMAIL", "접근 권한이 없는 사용자 요청입니다.");

    private final HttpStatus status;
    private final String code;
    private final String msg;
}
