package com.sw.newProject.service;

import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.mapper.MemberMapper;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private MemberMapper memberMapper;

    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public void insertMember(MemberDto memberDto) {
        memberMapper.insertMember(memberDto);
    }
}
