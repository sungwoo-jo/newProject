package com.sw.newProject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    @NotNull
    private int memNo;

    @NotNull(message = "아이디는 필수 입력 항목입니다.")
    @Size(min = 3, max = 20, message = "아이디는 최소 3자에서 최대 20자까지 입력 가능합니다.")
    private String memId;

    @NotNull(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 5, max = 50, message = "비밀번호는 최소 5자에서 최대 50자까지 입력 가능합니다.")
    private String memPw;

    @NotNull(message = "이름은 필수 입력 항목입니다.")
    @Size(min = 3, max = 10, message = "이름은 최소 3자에서 최대 10자까지 입력 가능합니다.")
    private String memNm;

    @NotNull(message = "닉네임은 필수 입력 항목입니다.")
    @Size(min = 3, max = 10, message = "닉네임은 최소 3자에서 최대 10자까지 입력 가능합니다.")
    private String nickNm;

    private String address1;
    private String address2;
    private String zipCode;
    private String phone;

    @Email(message = "이메일 규칙에 맞지 않습니다.")
    private String email;
    private String profilePath;
    private String profileImage;
    private Boolean deleteYn;
    private LocalDateTime regDt; // 날짜와 시간이 모두 필요
    private LocalDateTime modDt; // 날짜와 시간이 모두 필요
}
