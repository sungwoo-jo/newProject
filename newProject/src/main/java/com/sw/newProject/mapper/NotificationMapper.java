package com.sw.newProject.mapper;

import com.sw.newProject.dto.NotificationDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper {

    void saveNotify(NotificationDto notificationDto);
}
