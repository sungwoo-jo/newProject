package com.sw.newProject.service;

import com.sw.newProject.dto.DoResetPwDto;
import com.sw.newProject.dto.MailDto;
import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.dto.UploadFileDto;
import com.sw.newProject.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.security.SecureRandom;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberMapper memberMapper;
    private final MailDto mailDto;
    private final JavaMailSender javaMailSender;

    public void insertMember(MemberDto memberDto, MultipartFile file) throws Exception { // 회원가입 로직 처리
        System.out.println("[MemberService][insertMember][projectPath]: " + System.getProperty("user.dir"));
        System.out.println("[MemberService][insertMember][memberDto]: " + memberDto);
        System.out.println("[MemberService][insertMember][zipCode]: " + memberDto.getZipCode());
        memberDto.setMemPw(passwordEncrypt(memberDto.getMemPw()));
        memberMapper.insertMember(memberDto); // 회원 정보 저장

        // 프로필 이미지 저장 처리
        memberDto.setProfileImageName(saveProfileImage(memberDto, file));
        saveProfileImageName(memberDto);
    }

    private void saveProfileImageName(MemberDto memberDto) { // 프로필 이미지만 저장
        memberMapper.saveProfileImageName(memberDto);
    }

    /*
     * 프로필 이미지를 저장하는 메서드
     * file을 전달받아 uploadFile 테이블에 데이터를 저장하고
     * member 테이블에 profileImageName을 저장한다.
     * todo: 회원 별 폴더를 생성하고 폴더 내에 이미지를 저장하는 방식으로 전환해보기(이미지가 없다면 not found 나게끔)
     */
    public String saveProfileImage(MemberDto memberDto, MultipartFile file) throws IOException {
        UploadFileDto uploadFileDto = new UploadFileDto();

        String uuid = UUID.randomUUID().toString();

        // 확장자 구하기
        int pos = file.getOriginalFilename().lastIndexOf(".");
        String extension = file.getOriginalFilename().substring(pos+1);

        uploadFileDto.setUploadFileName(file.getOriginalFilename()); // 업로드 원본 파일명

        String directory = System.getProperty("user.dir") + "\\newProject\\src\\main\\resources\\static\\upload\\";

        // memNo의 디렉토리가 존재하는지 확인하고 없으면 생성
        File projectPath = new File(directory + memberDto.getMemNo() + "_" + memberDto.getMemId());
        if (!projectPath.exists()) {
            boolean created = projectPath.mkdirs(); // 디렉토리 생성
            if (!created) {
                throw new IOException("디렉토리를 생성할 수 없습니다.");
            }
        }
        uploadFileDto.setStoredFileName(projectPath + "\\" + uuid + "." + extension); // uuid로 파일명 변경하여 저장

        log.info("업로드 경로: {}", uploadFileDto.getStoredFileName());

        file.transferTo(new File(uploadFileDto.getStoredFileName()));
        memberMapper.saveProfileImage(uploadFileDto);

        return memberDto.getMemNo() + "_" + memberDto.getMemId() + "/" + uuid + "." + extension; // 회원 테이블에 저장할 파일명(폴더 + 파일명 + 확장자 형식으로 저장이 됨)
    }

    public List<MemberDto> getAllMember() {
        return memberMapper.getAllMember();
    }

    public MemberDto getMember(int memNo) {
        return memberMapper.getMember(memNo);
    }

    public void updateMember(@NotNull MemberDto reqMemberDto) {
        //  업데이트 하지 않는 값은 기존 값으로 두고, 업데이트 해야 하는 항목들은 업데이트 해주기
        //  업데이트 필요 항목: memPw, nickNm, address1, address2, zipCode, phone, email, profileImage, modDt / 불필요: memNo, memId, deleteYn, regDt
        MemberDto recentMemberDto = getMember(reqMemberDto.getMemNo()); // 기존 정보 조회
        MemberDto updateMemberDto = new MemberDto(); // 리턴할 데이터
        LocalDateTime nowDateTime = LocalDateTime.now();

        // 기존 값으로 유지
        updateMemberDto.setMemNo(recentMemberDto.getMemNo());
        updateMemberDto.setMemId(recentMemberDto.getMemId());
        updateMemberDto.setMemNm(recentMemberDto.getMemNm());
        updateMemberDto.setDeleteYn(recentMemberDto.getDeleteYn());
        updateMemberDto.setRegDt(recentMemberDto.getRegDt());

        // 전달받은 값으로 업데이트
        if (reqMemberDto.getMemPw() != null && !reqMemberDto.getMemPw().equals("")) {
            updateMemberDto.setMemPw(reqMemberDto.getMemPw());
        } else {
            updateMemberDto.setMemPw(recentMemberDto.getMemPw());
        }
        if (reqMemberDto.getNickNm() != null && !reqMemberDto.getNickNm().equals("")) {
            updateMemberDto.setNickNm(reqMemberDto.getNickNm());
        } else {
            updateMemberDto.setNickNm(recentMemberDto.getNickNm());
        }
        if (reqMemberDto.getAddress1() != null && !reqMemberDto.getAddress1().equals("")) {
            updateMemberDto.setAddress1(reqMemberDto.getAddress1());
        } else {
            updateMemberDto.setAddress1(recentMemberDto.getAddress1());
        }
        if (reqMemberDto.getAddress2() != null && !reqMemberDto.getAddress2().equals("")) {
            updateMemberDto.setAddress2(reqMemberDto.getAddress2());
        } else {
            updateMemberDto.setAddress2(recentMemberDto.getAddress2());
        }
        if (reqMemberDto.getZipCode() != null && !reqMemberDto.getZipCode().equals("")) {
            updateMemberDto.setZipCode(reqMemberDto.getZipCode());
        } else {
            updateMemberDto.setZipCode(recentMemberDto.getZipCode());
        }
        if (reqMemberDto.getPhone() != null && !reqMemberDto.getPhone().equals("")) {
            updateMemberDto.setPhone(reqMemberDto.getPhone());
        } else {
            updateMemberDto.setPhone(recentMemberDto.getPhone());
        }
        if (reqMemberDto.getEmail() != null && !reqMemberDto.getEmail().equals("")) {
            updateMemberDto.setEmail(reqMemberDto.getEmail());
        } else {
            updateMemberDto.setEmail(recentMemberDto.getEmail());
        }
        if (reqMemberDto.getProfileImageName() != null && !reqMemberDto.getProfileImageName().equals("")) {
            updateMemberDto.setProfileImageName(reqMemberDto.getProfileImageName());
        } else {
            updateMemberDto.setProfileImageName(recentMemberDto.getProfileImageName());
        }
        if (reqMemberDto.getComm() != null && !reqMemberDto.getComm().equals("")) {
            updateMemberDto.setComm(reqMemberDto.getComm());
        } else {
            updateMemberDto.setComm(recentMemberDto.getComm());
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

    public MemberDto doLogin(MemberDto memberDto) throws NoSuchAlgorithmException {
        String memPw = passwordEncrypt(memberDto.getMemPw()); // 암호화 처리
        memberDto.setMemPw(memPw); // 암호화 처리 된 비밀번호로 set
        System.out.println("[MemberService][doLogin][memId, memPw]: " + memberDto.getMemId() + ", " + memberDto.getMemPw());
        return memberMapper.doLogin(memberDto);
    }

    public String findId(String memNm, String email) { // 이름과 이메일을 이용해 아이디 조회
        return memberMapper.findId(memNm, email);
    }

    @Async
    public Future<String> sendMailFindId(MemberDto memberDto) { // 찾은 아이디를 메일로 발송하는 메서드
        String type = "findId";
        System.out.println("sendMailFindId 실행");
        System.out.println("[MemberService][doFindId][memId]: " + memberDto.getMemId());
        memberDto.setMemId(findId(memberDto.getMemNm(), memberDto.getEmail())); // 이메일 전송 전 아이디 set

        try {
            System.out.println("sendEmail() 실행 시작");
            return CompletableFuture.completedFuture(sendEmail(type, memberDto.getEmail(), "[newProject] 아이디 찾기 결과", "귀하의 아이디는 " + memberDto.getMemId() + " 입니다."));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("sendEmail() 실행 종료");
        return null;
    }

    public Integer findPw(String memNm, String email, String memId) {
        return memberMapper.findPw(memNm, email, memId);
    }

    @Async
    public Future<String> sendMailFindPw(MemberDto memberDto) throws NoSuchAlgorithmException {
        String type = "findPw";
        System.out.println("doFindPw 실행");
        DoResetPwDto doResetPwDto = new DoResetPwDto(); // 새로운 비밀번호를 세팅해줄 Dto 생성
        System.out.println("[MemberService][doFindPw][memNm, email, memId]: " + memberDto.getMemNm() + ", " + memberDto.getEmail() + ", " + memberDto.getMemId());

        // 비밀번호 변경 대상 회원의 memNo 를 이용해 새로운 비밀번호를 생성하고, 암호화 처리 후 DB에 set 하고 이메일 발송 처리 로직
        doResetPwDto.setMemNo(findPw(memberDto.getMemNm(), memberDto.getEmail(), memberDto.getMemId())); // 현재 회원의 memNo 구하기
        doResetPwDto.setNewPw(generateRandomPassword(12)); // 새로운 비밀번호 생성
        String unEncryptPw = doResetPwDto.getNewPw();
        doResetPwDto.setNewPw(passwordEncrypt(doResetPwDto.getNewPw())); // 암호화 처리
        doResetPw(doResetPwDto); // 암호화 처리 된 비밀번호 넘기기
        try { // 메일 발송
            System.out.println("sendEmail() 실행 시작");
            return CompletableFuture.completedFuture(sendEmail(type, memberDto.getEmail(), "[newProject] 비밀번호 재설정 결과", "귀하의 임시비밀번호는 " + unEncryptPw + " 입니다."));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("sendEmail() 실행 종료");
        return null;
    }

    public void doResetPw(DoResetPwDto doResetPwDto) throws NoSuchAlgorithmException {
        System.out.println("[MemberService][doResetPw][doResetPwDto]: " + doResetPwDto.getMemNo() + ", " + doResetPwDto.getNewPw() + ", " + doResetPwDto.getMemNo());
        memberMapper.doResetPw(doResetPwDto);
    }

    public File saveImage(MultipartFile profileImage) throws Exception {
        String projectPath = System.getProperty("user.dir") + File.separator + "profileImage" + File.separator;
        System.out.println(projectPath);

        // 디렉토리가 존재하는지 확인하고 없으면 생성
        File directory = new File(projectPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs(); // 디렉토리 생성
            if (!created) {
                throw new IOException("디렉토리를 생성할 수 없습니다.");
            }
        }

        UUID uuid = UUID.randomUUID(); // 랜덤 이름 생성
        String fileName = uuid + "_" + profileImage.getOriginalFilename();

        //파일을 담을 껍데기를 만들어 파일경로와 파일이름을 매개변수로 넣고
        File saveFile = new File(projectPath, fileName);
        //업로드된 파일을 껍데기에 담아 저장해준다
        profileImage.transferTo(saveFile);

        System.out.println("[MemberService][saveImage][saveFile]: " + saveFile);
        return saveFile;
    }

    public String sendEmail(String type, String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("jason692193@gmail.com");

        try {
            System.out.println("[메일 발송 성공]");
            javaMailSender.send(message);
            System.out.println("[" + type + "] 메일 발송 결과: " + "to: " + to + "\t" + "subject: " + subject + "\t" + "text: " + text);
        } catch (MailException e) {
            System.out.println("[메일 발송 실패]");
            e.printStackTrace();
            System.out.println("Error sending email");
        }
        return "[MemberService][sendEmail] 종료";
    }

    // 랜덤 비밀번호 생성 함수 (특수문자 포함)
    public String generateRandomPassword(int length) {
        // 사용할 문자 집합 (대소문자, 숫자, 특수문자 포함)
        String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(charset.length());
            password.append(charset.charAt(randomIndex));  // 랜덤 문자를 선택하여 추가
        }

        return password.toString();
    }
}

