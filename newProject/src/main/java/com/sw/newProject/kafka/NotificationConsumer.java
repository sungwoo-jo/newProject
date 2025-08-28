package com.sw.newProject.kafka;

import com.sw.newProject.dto.NotificationDto;
import com.sw.newProject.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "notification-topic", groupId = "newProject-group")
    public void consume(NotificationDto notificationDto) {
        log.info("Consumed message: {}", notificationDto.getContent());

        // 카프카로부터 받은 메시지로 실제 SSE 알림 전송
        notificationService.notifyOne(
                notificationDto.getFromMemNo(),
                notificationDto.getContent(),
                notificationDto.getNotificationType()
        );
    }
}
