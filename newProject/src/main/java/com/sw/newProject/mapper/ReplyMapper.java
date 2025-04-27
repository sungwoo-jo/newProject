package com.sw.newProject.mapper;

import com.sw.newProject.dto.ReplyDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ReplyMapper {
    List<ReplyDto> getReply(HashMap<String, Object> map);

    void doWrite(ReplyDto replyDto);

    void doDelete(HashMap<String, Object> map);

    void doUpdate(HashMap<String, Object> map);

    Integer getWriterNo(HashMap<String, Object> map);

    Integer getMaxReplySeqNo(HashMap<String, Object> map);
}
