package com.sw.newProject.mapper;

import com.sw.newProject.dto.NotificationDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface NotificationMapper {

    void saveNotify(NotificationDto notificationDto);

    ArrayList<NotificationDto> getRecentNotification(Integer memNo);
}
