package com.sw.newProject.mapper;

import com.sw.newProject.dto.DoResetPwDto;
import com.sw.newProject.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    void insertMember(MemberDto memberDto);

    List<MemberDto> getAllMember();

    void updateMember(MemberDto memberDto);

    MemberDto getMember(int memNo);

    void deleteMember(Integer memNo);

    Long duplicationIdCheck(String memId);

    Long duplicationNickNmCheck(String nickNm);

    Long duplicationEmailCheck(String email);

    MemberDto doLogin(MemberDto memberDto);

    Integer validationMemId(String memId);

    Integer validationMemPw(String memPw);

    String findId(String memNm, String email);

    Integer findPw(String memNm, String email, String memId);

    void doResetPw(DoResetPwDto doResetPwDto);
}
