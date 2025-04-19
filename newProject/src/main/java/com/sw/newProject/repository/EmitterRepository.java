package com.sw.newProject.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class EmitterRepository {
    // 모든 Emitters를 저장하는 ConcurrentHashMap
    private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();

    /**
     * 주어진 아이디와 이미터를 저장
     *
     * @param memNo -   회원 번호.
     * @param emitter - 이벤트 Emitter.
     */
    public void save(Integer memNo, SseEmitter emitter) {
        emitters.put(memNo, emitter);
    }

    /**
     * 주어진 아이디의 Emitter를 제거
     *
     * @param memNo - 회원 번호.
     */
    public void deleteById(Integer memNo) {
        emitters.remove(memNo);
    }

    /**
     * 주어진 아이디의 Emitter를 가져옴.
     *
     * @param memNo - 회원 번호.
     * @return SseEmitter - 이벤트 Emitter.
     */
    public SseEmitter get(Integer memNo) {
        return emitters.get(memNo);
    }

    public Map<Integer, SseEmitter> findAll() {
        return emitters;
    }
}