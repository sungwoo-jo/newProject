package com.sw.newProject.service;

import com.sw.newProject.dto.ReplyDto;
import com.sw.newProject.mapper.ReplyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyMapper replyMapper;

    public List<ReplyDto> getReply(HashMap<String, Object> map) {
        return replyMapper.getReply(map);
    }

    public void doWrite(HashMap<String, Object> map) {
        replyMapper.doWrite(map);
    }

    public void doDelete(HashMap<String, Object> map) {
        replyMapper.doDelete(map);
    }

    public void doUpdate(HashMap<String, Object> map) {
        replyMapper.doUpdate(map);
    }
}
