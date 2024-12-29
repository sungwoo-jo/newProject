package com.sw.newProject.mapper;

import com.sw.newProject.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    void insertMember(MemberDto memberDto);

    List<MemberDto> getAllMember();
}
