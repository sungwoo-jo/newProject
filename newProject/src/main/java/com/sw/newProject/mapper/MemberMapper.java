package com.sw.newProject.mapper;

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

    Integer validationMemId(String memId);

    Integer validationMemPw(String memPw);

    MemberDto doLogin(String memId, String memPw);

//    boolean validationPassword(MemberDto memberDto);
}
