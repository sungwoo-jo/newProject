package com.sw.newProject.service;

import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.mapper.MemberMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
public class MemberService {

    private final MemberMapper memberMapper;

    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public void insertMember(MemberDto memberDto) {
        System.out.println("insert Member" + memberDto);
        memberMapper.insertMember(memberDto);
    }

    public List<MemberDto> getAllMember() {
        return memberMapper.getAllMember();
    }

    public MemberDto getMember(int memNo) {
        return memberMapper.getMember(memNo);
    }

    public void updateMember(@NotNull MemberDto reqMemberDto) {
        //  업데이트 하지 않는 값은 기존 값으로 두고, 업데이트 해야 하는 항목들은 업데이트 해주기
        //  업데이트 필요 항목: memPw, nickNm, address1, address2, zipCode, phone, email, profilePath, modDt / 불필요: memNo, memId, deleteYn, regDt
        MemberDto recentMemberDto = getMember(reqMemberDto.getMemNo()); // 기존 정보 조회
        MemberDto updateMemberDto = new MemberDto(); // 리턴할 데이터
        LocalDateTime nowDateTime = LocalDateTime.now();

        // 기존 값으로 유지
        updateMemberDto.setMemNo(recentMemberDto.getMemNo());
        updateMemberDto.setMemId(recentMemberDto.getMemId());
        updateMemberDto.setDeleteYn(recentMemberDto.getDeleteYn());
        updateMemberDto.setRegDt(recentMemberDto.getRegDt());
        updateMemberDto.setModDt(nowDateTime);

        // 전달받은 값으로 업데이트
        if (reqMemberDto.getMemPw() != null) {
            updateMemberDto.setMemPw(reqMemberDto.getMemPw());
        }
        if (reqMemberDto.getNickNm() != null) {
            updateMemberDto.setNickNm(reqMemberDto.getNickNm());
        }
        if (reqMemberDto.getAddress1() != null) {
            updateMemberDto.setAddress1(reqMemberDto.getAddress1());
        }
        if (reqMemberDto.getAddress2() != null) {
            updateMemberDto.setAddress2(reqMemberDto.getAddress2());
        }
        if (reqMemberDto.getZipCode() != null) {
            updateMemberDto.setZipCode(reqMemberDto.getZipCode());
        }
        if (reqMemberDto.getPhone() != null) {
            updateMemberDto.setPhone(reqMemberDto.getPhone());
        }
        if (reqMemberDto.getEmail() != null) {
            updateMemberDto.setEmail(reqMemberDto.getEmail());
        }
        if (reqMemberDto.getProfilePath() != null) {
            updateMemberDto.setProfilePath(reqMemberDto.getProfilePath());
        }

        memberMapper.updateMember(updateMemberDto); // 새롭게 세팅한 값으로 업데이트
    }

    public void deleteMember(Integer memNo) {
        if (memNo == null) {
            System.out.println("memNo is null");
        }
        else {
            memberMapper.deleteMember(memNo);
        }
    }
}
