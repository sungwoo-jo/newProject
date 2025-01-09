package com.sw.newProject.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

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

    public void insertMember(MemberDto memberDto, MultipartFile profileImage) throws Exception { // 회원가입 로직 처리
        System.out.println("[MemberService][insertMember][projectPath]: " + System.getProperty("user.dir"));
        System.out.println("[MemberService][insertMember][profileImage]: " + profileImage);
        System.out.println("[MemberService][insertMember][memberDto]: " + memberDto);
        System.out.println("[MemberService][insertMember][zipCode]: " + memberDto.getZipCode());
         // 프로필 이미지 저장 처리
        saveImage(profileImage);

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
        if (reqMemberDto.getProfileImage() != null) {
            updateMemberDto.setProfileImage(reqMemberDto.getProfileImage());
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

    public String doFindId(String memNm, String email) throws MessagingException {
        String type = "findId";
        String memId = memberMapper.doFindId(memNm, email);
        System.out.println("[MemberService][doFindId][memId]: " + memId);
        System.out.println("doFindIdOfEmail 실행");
        doFindIdOfEmail("테스트1", "sungwoo9671@naver.com");

        System.out.println("sendEmail() 실행 시작");
        sendEmail(type, email, "[newProject] 아이디 찾기 결과", "귀하의 아이디는 " + memId + " 입니다.");
        System.out.println("sendEmail() 실행 종료");

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

    public void sendEmail(String type, String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("jason692193@gmail.com");

        try {
            javaMailSender.send(message);
            System.out.println("[" + type + "] 메일 발송 성공: " + "to: " + to + "\t" + "subject: " + subject + "\t" + "text: " + text);
        } catch (MailException e) {
            e.printStackTrace();
            System.out.println("Error sending email");
        }
    }
}

