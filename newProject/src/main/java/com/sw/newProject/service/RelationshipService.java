package com.sw.newProject.service;

import com.sw.newProject.dto.AppendTargetToJsonDto;
import com.sw.newProject.dto.BoardDto;
import com.sw.newProject.dto.FriendShipDto;
import com.sw.newProject.dto.MemberDto;
import com.sw.newProject.mapper.RelationshipMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RelationshipService {

    private final RelationshipMapper relationshipMapper;

    public List<String> getJsonKeysList(int memNo) {
        return relationshipMapper.getJsonKeysList(memNo);
    }

    public void appendMemNoToJson(AppendTargetToJsonDto appendTargetToJsonDto) {
        appendTargetToJson(appendTargetToJsonDto);
    }

    public void appendTargetToJson(AppendTargetToJsonDto appendTargetToJsonDto) { // JSON key에 memNo를 추가
        relationshipMapper.appendTargetToJson(appendTargetToJsonDto);
    }

    @Transactional
    public void doFollow(MemberDto reqMember, BoardDto accMember) {
        log.info("reqMember: {}", reqMember.getMemNo());
        log.info("accMember: {}", accMember.getMemNo());

        List<String> reqMemberFollowData = getJsonKeysList(reqMember.getMemNo()); // 요청자의 현재 팔로우 데이터를 가져오기
        List<String> accMemberFollowingData = getJsonKeysList(accMember.getMemNo()); // 수락자의 현재 팔로잉 데이터를 가져오기

        if (reqMemberFollowData.contains(accMember.getMemNo())) { // 요청자의 팔로우 데이터 중 수락자가 존재하는 지 확인
            ResponseEntity.badRequest();
        } else { // 요청자 팔로우 데이터에 대상자를 추가
            AppendTargetToJsonDto appendTargetToJsonDto = new AppendTargetToJsonDto();
            appendTargetToJsonDto.setOwnerMemNo(reqMember.getMemNo());
            appendTargetToJsonDto.setTargetMemNo(accMember.getMemNo());
            appendTargetToJsonDto.setColumnName("follow");
            appendMemNoToJson(appendTargetToJsonDto);
        }

        if (accMemberFollowingData.contains(reqMember.getMemNo())) { // 수락자의 팔로잉 데이터 중 요청자가 존재하는 지 확인
            ResponseEntity.badRequest();
        } else { // 수락자 팔로잉 데이터에 요청자를 추가
            AppendTargetToJsonDto appendTargetToJsonDto = new AppendTargetToJsonDto();
            appendTargetToJsonDto.setOwnerMemNo(accMember.getMemNo());
            appendTargetToJsonDto.setTargetMemNo(reqMember.getMemNo());
            appendTargetToJsonDto.setColumnName("following");
            appendMemNoToJson(appendTargetToJsonDto);
        }
    }

    public void addFriendFromMember(FriendShipDto friendShipDto) {
        relationshipMapper.addFriendFromMember(friendShipDto);
    }

    public void addFriendToMember(FriendShipDto friendShipDto) {
        relationshipMapper.addFriendToMember(friendShipDto);
    }
}
