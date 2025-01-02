package com.sw.newProject.service;

import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.mapper.MemberMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class MemberService {

    private final MemberMapper memberMapper;

    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public void insertMember(MemberDto memberDto) { // 회원가입 로직 처리
        System.out.println("[MemberService][insertMember][memberDto]: " + memberDto);
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
            System.out.println("[MemberService][deleteMember][memNo]: memNo is null");
        }
        else {
            memberMapper.deleteMember(memNo);
            System.out.println("[MemberService][deleteMember][" + memNo + "]: deleteSuccess");
        }
    }

    public Long duplicationIdCheck(String memId) { // 중복 회원 검증
        System.out.println("[MemberService][duplicationIdCheck][memId]: " + memId);
        return memberMapper.duplicationIdCheck(memId);
    }

    public String passwordEncrypt(String memPw) throws NoSuchAlgorithmException { // SHA-256 비밀번호 암호화
        MessageDigest digest = MessageDigest.getInstance("SHA-256"); // SHA-256 해시 알고리즘 인스턴스 생성

        // 비밀번호를 바이트 배열로 변환하고, 해시 계산
        byte[] hashBytes = digest.digest(memPw.getBytes());

        // 해시된 결과를 16진수 문자열로 변환
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
//        System.out.println("[MemberService][passwordEncrypt][hexString.toString()]: " + hexString.toString());
        return hexString.toString();
    }

    public Long duplicationNickNmCheck(String nickNm) {
        System.out.println("[MemberService][duplicationNickNmCheck][nickNm]: " + nickNm);
        return memberMapper.duplicationNickNmCheck(nickNm);
    }

    public Long duplicationEmailCheck(String email) {
        System.out.println("[MemberService][duplicationNickNmCheck][email]: " + email);
        return memberMapper.duplicationEmailCheck(email);
    }

    public MemberDto doLogin(String memId, String memPw) throws NoSuchAlgorithmException {
        memPw = passwordEncrypt(memPw);
        System.out.println("[MemberService][doLogin][memId, memPw]: " + memId + ", " + memPw);
        return memberMapper.doLogin(memId, memPw);
    }

    public String doFindId(String memNm, String email) {
        String memId = memberMapper.doFindId(memNm, email);
        System.out.println("[MemberService][doFindId][memId]: " + memId);
        return memId;
    }

    public Integer doFindPw(String memNm, String email, String memId) {
        return memberMapper.doFindPw(memNm, email, memId);
    }

    public void doResetPw(Integer memNo, String newPw) throws NoSuchAlgorithmException {
        Map<String, Object> map = new HashMap<>();
        newPw = passwordEncrypt(newPw);
        map.put("memNo", memNo);
        map.put("newPw", newPw);
        memberMapper.doResetPw(map);
    }
//    public Boolean doFindIdOfEmail(String memNm, String email) { // 이거는 메일 발송 API 구현 후 사용하기(그전까지는 doFindId 사용)
//        }
//        JavaMailSender emailSender = null;
//        System.out.println("[MemberService][doFindId][memId]: " + memberMapper.doFindId(memNm, email));
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("jason692193@gmail.com");
//        message.setTo(email);
//        message.setSubject("아이디 찾기 결과");
//        message.setText("귀하의 아이디는: test");
//
//        emailSender.send(message);
//        return true;
//    }

}
