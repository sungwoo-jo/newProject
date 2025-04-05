package com.sw.newProject.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.List;

@Data
public class SaveEntrantDto { // 채팅방 참여자 정보 저장(chatinfo.entrant)

    private Integer sno; // chatinfo.sno

    private List<Integer> entrant; // chatinfo.entrant

    private String data;

    public JsonNode getJsonData() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(data);
    }

    public void setJsonData(JsonNode jsonData) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        this.data = objectMapper.writeValueAsString(jsonData);
    }
}
