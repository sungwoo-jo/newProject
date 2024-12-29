package com.sw.newProject.service;

import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.mapper.MemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class MemberService {

    private final MemberMapper memberMapper;

    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public void insertMember(MemberDto memberDto) {
        memberMapper.insertMember(memberDto);
    }

    public List<MemberDto> getAllMember() {
        return memberMapper.getAllMember();
    }
}
