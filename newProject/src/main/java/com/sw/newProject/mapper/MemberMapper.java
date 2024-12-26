package com.sw.newProject.mapper;

import com.sw.newProject.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    void insertMember(MemberDto memberDto);
}
