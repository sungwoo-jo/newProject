package com.sw.newProject.mapper;

import com.sw.newProject.dto.SaveEntrantDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface ChatMapper {
    void saveRoomInfo(HashMap<String, Object> map);

    void saveEntrant(SaveEntrantDto saveEntrantDto);
}
