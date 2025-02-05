package com.sw.newProject.service;

import com.sw.newProject.dto.PostDto;
import com.sw.newProject.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostMapper postMapper;

    public List<PostDto> getPostList(Integer memNo) {
        return postMapper.getPostList(memNo);
    }

    public PostDto viewPost(Integer postNo) {
        return postMapper.viewPost(postNo);
    }

    public Integer doDelete(Integer postNo) {
        return postMapper.doDelete(postNo);
    }

    public Integer doWrite(PostDto postDto) {
        return postMapper.doWrite(postDto);
    }

    public Integer getMemNoOfMemId(String memId) {
        return postMapper.getMemNoOfMemId(memId);
    }
}
