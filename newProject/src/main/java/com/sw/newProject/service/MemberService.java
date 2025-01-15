package com.sw.newProject.service;

import com.sw.newProject.dto.DoResetPwDto;
import com.sw.newProject.dto.MailDto;
import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.mapper.MemberMapper;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
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

@Transactional
@Service
public class MemberService {

    private final MemberMapper memberMapper;
    private final MailDto mailDto;
    private final JavaMailSender javaMailSender;


    public MemberService(MemberMapper memberMapper, MailDto mailDto, JavaMailSender javaMailSender) {
        this.memberMapper = memberMapper;
        this.mailDto = mailDto;
        this.javaMailSender = javaMailSender;
    }

    public void insertMember(MemberDto memberDto) throws Exception { // 회원가입 로직 처리
        System.out.println("[MemberService][insertMember][projectPath]: " + System.getProperty("user.dir"));
        System.out.println("[MemberService][insertMember][profileImage]: " + memberDto.getProfileImage());
        System.out.println("[MemberService][insertMember][memberDto]: " + memberDto);
        System.out.println("[MemberService][insertMember][zipCode]: " + memberDto.getZipCode());
         // 프로필 이미지 저장 처리
//        memberDto.setProfileImage((MultipartFile) saveImage(memberDto.getProfileImage()));

//        memberDto.setProfilePath(profileInfo.getPath());
//        memberDto.setProfileImage(profileInfo.getName());
        memberMapper.insertMember(memberDto);
        System.out.println("insertMember 끝");
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
        updateMemberDto.setModDt(nowDateTime);

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
        if (reqMemberDto.getProfileImage() != null && !reqMemberDto.getProfileImage().equals("")) {
            updateMemberDto.setProfileImage(reqMemberDto.getProfileImage());
        } else {
            updateMemberDto.setProfileImage(recentMemberDto.getProfileImage());
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

    public MemberDto doLogin(MemberDto memberDto) throws NoSuchAlgorithmException {
//        String memPw = passwordEncrypt(memberDto.getMemPw()); // 암호화 처리
        memberDto.setMemPw(memberDto.getMemPw());
        System.out.println("[MemberService][doLogin][memId, memPw]: " + memberDto.getMemId() + ", " + memberDto.getMemPw());
        return memberMapper.doLogin(memberDto);
    }

    @Async
    public Future<String> doFindId(String memNm, String email) throws MessagingException {
        String type = "findId";
        System.out.println("doFindId 실행");
        String memId = memberMapper.doFindId(memNm, email);
        System.out.println("[MemberService][doFindId][memId]: " + memId);
        System.out.println("doFindIdOfEmail 실행");
        doFindIdOfEmail("테스트1", "sungwoo9671@naver.com");

        try {
            System.out.println("sendEmail() 실행 시작");
            return CompletableFuture.completedFuture(sendEmail(type, email, "[newProject] 아이디 찾기 결과", "귀하의 아이디는 " + memId + " 입니다."));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("sendEmail() 실행 종료");
        return null;
    }

    @Async
    public Future<String> doFindPw(String memNm, String email, String memId) throws NoSuchAlgorithmException {
        String type = "findPw";
        System.out.println("doFindPw 실행");
        DoResetPwDto doResetPwDto = new DoResetPwDto();
        System.out.println("[MemberService][doFindPw][memNm, email, memId]: " + memNm + ", " + email + ", " + memId);
        Integer memNo = memberMapper.doFindPw(memNm, email, memId);
        doResetPwDto.setMemNo(memNo);
        doResetPwDto.setNewPw(generateRandomPassword(12));
        String beforeMemPw = doResetPwDto.getNewPw(); // 암호화 전 비밀번호
        doResetPwDto.setNewPw(passwordEncrypt(doResetPwDto.getNewPw())); // 암호화 처리
        doResetPw(doResetPwDto); // 비밀번호 업데이트
        try { // 메일 발송
            System.out.println("sendEmail() 실행 시작");
            return CompletableFuture.completedFuture(sendEmail(type, email, "[newProject] 비밀번호 재설정 결과", "귀하의 임시비밀번호는 " + beforeMemPw + " 입니다."));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("sendEmail() 실행 종료");
        return null;
    }

    public void doResetPw(DoResetPwDto doResetPwDto) throws NoSuchAlgorithmException {
        String newPw = passwordEncrypt(doResetPwDto.getNewPw());
        System.out.println("[MemberService][doResetPw][doResetPwDto]: " + doResetPwDto.getMemNo() + ", " + doResetPwDto.getNewPw());
        memberMapper.doResetPw(doResetPwDto);
    }
    public void doFindIdOfEmail(String memNm, String email) throws MessagingException { // 이거는 메일 발송 API 구현 후 사용하기(그전까지는 doFindId 사용)
        System.out.println("doFindIdOfEmail 메서드 내부");

        String receiver = email;
        String senderMail = mailDto.getSenderMail();
        String password = mailDto.getPassword();

        Properties props = new Properties();
        props.setProperty("mail.smtp.host", mailDto.getHost());
        props.setProperty("mail.smtp.port", mailDto.getPort());
        props.setProperty("mail.smtp.auth", mailDto.getAuth());
        props.setProperty("mail.smtp.starttls.enable", mailDto.getEnable());

        System.out.println(props.getProperty("mail.smtp.host"));
        System.out.println(props.getProperty("mail.smtp.port"));
        System.out.println(props.getProperty("mail.smtp.auth"));
        System.out.println(props.getProperty("mail.smtp.starttls.enable"));

        // 보내는 사람 계정 정보 설정
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderMail, password);
            }
        });

        System.out.println("[MemberService][doFindId][memId]: " + memberMapper.doFindId(memNm, email));

        // 메일 내용 작성
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(senderMail));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
        msg.setSubject("아이디 찾기 결과 메일");
        msg.setText("귀하의 아이디는 " + "test1" + "입니다.");

        System.out.println("[MemberService][doFindIdOfEmail] 이메일 전송 완료(이제사용안함)");
    }

    public File saveImage(MultipartFile profileImage) throws Exception {
        // System.getProperty("user.dir") : 현재 프로젝트 경로를 가져옴
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

