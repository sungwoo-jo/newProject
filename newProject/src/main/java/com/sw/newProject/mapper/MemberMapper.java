package com.sw.newProject.mapper;

import com.sw.newProject.dto.AppendTargetToJsonDto;
import com.sw.newProject.dto.DoResetPwDto;
import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.dto.UploadFileDto;
import org.apache.ibatis.annotations.Mapper;
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

    String findId(String memNm, String email);

    Integer findPw(String memNm, String email, String memId);

    void doResetPw(DoResetPwDto doResetPwDto);

    void saveProfileImage(UploadFileDto uploadFileDto);

    void saveProfileImageName(MemberDto memberDto);

    HashMap<String, String> getFollowData(Integer memNo);

    void doCancelFollow(HashMap<String, Object> map);

    void doCancelFollowing(HashMap<String, Object> map);

    List<String> getJsonKeysList(int memNo);

    void appendTargetToJson(AppendTargetToJsonDto appendTargetToJsonDto);
}
