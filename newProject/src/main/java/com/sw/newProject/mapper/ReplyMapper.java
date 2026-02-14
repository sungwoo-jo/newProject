package com.sw.newProject.mapper;

import com.sw.newProject.dto.reply.GetReplyDto;
import com.sw.newProject.dto.reply.ReplyDto;
import com.sw.newProject.dto.reply.TargetOrderDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ReplyMapper {
    List<GetReplyDto> getReply(ReplyDto dto);

    void doWrite(ReplyDto replyDto);

    void doDelete(HashMap<String, Object> map);

    void doUpdate(HashMap<String, Object> map);

    Integer getWriterNo(HashMap<String, Object> map);

    Integer getMaxGroupNo(ReplyDto replyDto);

    Integer getParentGroupNo(Integer parentNo);

    ReplyDto getParentReplyInfo(Integer parentNo);

    void updateParentAnswerCnt(Integer parentNo);

    Integer getMaxGroupOrder(ReplyDto replyDto);

    void plusGroupOrder(ReplyDto replyDto);

    Integer existGroupOrder(ReplyDto parentReplyDto);

    Integer existParentNo(ReplyDto parentReplyDto);

    Integer existBeforeReply(ReplyDto replyDto);

    Integer getTargetOrder(TargetOrderDto targetOrderDto);

    int updateExistingReply(TargetOrderDto targetOrderDto);
}
