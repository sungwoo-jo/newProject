package com.sw.newProject.mapper;

import com.sw.newProject.dto.PostDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    List<PostDto> getPostList(Integer memNo);

    PostDto viewPost(Integer postNo);

    Integer doDelete(Integer postNo);

    Integer doWrite(PostDto postDto);
}
