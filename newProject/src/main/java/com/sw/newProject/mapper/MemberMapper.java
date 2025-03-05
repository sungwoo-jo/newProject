package com.sw.newProject.mapper;

import com.sw.newProject.dto.DoResetPwDto;
import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.dto.UploadFileDto;
import org.apache.ibatis.annotations.Mapper;
import org.json.JSONObject;

import java.util.HashMap;
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

    void saveProfileImage(UploadFileDto uploadFileDto);

    void saveProfileImageName(MemberDto memberDto);

    void insertFollowData(HashMap<String, String> followData);

    void insertFollowingData(HashMap<String, String> followingData);

    HashMap<String, String> getFollowData(Integer memNo);

    HashMap<String, String> getFollowingData(Integer memNo);

    void doCancelFollow(HashMap<String, Object> map);

    void doCancelFollowing(HashMap<String, Object> map);
}
