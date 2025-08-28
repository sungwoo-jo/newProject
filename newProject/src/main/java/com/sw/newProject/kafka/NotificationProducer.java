package com.sw.newProject.kafka;

import com.sw.newProject.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private static final String TOPIC = "notification-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendNotification(NotificationDto notificationDto) {
        log.info("Producing message: {}", notificationDto.getContent());
        this.kafkaTemplate.send(TOPIC, notificationDto);
    }
}
