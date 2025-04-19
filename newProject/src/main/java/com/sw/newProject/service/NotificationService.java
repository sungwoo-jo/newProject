package com.sw.newProject.service;

import com.sw.newProject.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    // 기본 타임아웃 설정
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;

    /**
     * 클라이언트가 구독을 위해 호출하는 메서드.
     *
     * @param memNo - 구독하는 클라이언트 회원의 번호.
     * @return SseEmitter - 서버에서 보낸 이벤트 Emitter
     */
    public SseEmitter subscribe(Integer memNo) {
        log.info("-- execute subscribe method: memNo=" + memNo + " --");
        SseEmitter emitter = createEmitter(memNo);
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("sse")
                        .data("send dummy-data"));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    /**
     * 서버의 이벤트를 클라이언트에게 보내는 메서드
     * 다른 서비스 로직에서 이 메서드를 사용해 데이터를 Object event에 넣고 전송하면 된다.
     *
     * @param memNo - 메세지를 전송할 회원의 번호.
     * @param event  - 전송할 이벤트 객체.
     */
    public void notifyOne(Integer memNo, Object event) {
        sendToClient(memNo, event);
        log.info("{} 에게 {} 알림 전송 완료", memNo, event);
    }

    /**
     * 서버의 이벤트를 모든 클라이언트에게 보내는 메서드
     * 다른 서비스 로직에서 이 메서드를 사용해 데이터를 Object event에 넣고 전송하면 된다.
     *
     * @param memNo - 메세지를 전송할 회원의 번호.
     * @param event  - 전송할 이벤트 객체.
     */
//    public void notifyAll(Integer memNo, Object event) {
//        emitterRepository.findAll().forEach((memNo, emitter));
//        sendToClient(memNo, event);
//    }

    /**
     * 클라이언트에게 데이터를 전송
     *
     * @param memNo   - 데이터를 받을 회원의 번호.
     * @param data - 전송할 데이터.
     */
    private void sendToClient(Integer memNo, Object data) {
        SseEmitter emitter = emitterRepository.get(memNo);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().id(String.valueOf(memNo)).name("notify").data(data)); // 여기서 설정한 eventName인 sse가 프론트의 addEventListener 의 값이 된다.
            } catch (IOException exception) {
                emitterRepository.deleteById(memNo);
                emitter.completeWithError(exception);
            }
        }
    }

    /**
     * 사용자 아이디를 기반으로 이벤트 Emitter를 생성
     *
     * @param memNo - 회원의 번호.
     * @return SseEmitter - 생성된 이벤트 Emitter.
     */
    private SseEmitter createEmitter(Integer memNo) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(memNo, emitter);

        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
        emitter.onCompletion(() -> emitterRepository.deleteById(memNo));
        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout(() -> emitterRepository.deleteById(memNo));

        return emitter;
    }

    public void sendNotify(Set<Integer> friendList) {
        log.info("call sendNotify");
        log.info("friendList: {}", friendList);
        log.info("emitterRepository.findAll: {}", emitterRepository.findAll());

        for (Integer friend : friendList) {
            notifyOne(friend, emitterRepository.get(friend));
        }
    }
}