package com.ssafy.tingbackend.chatting.service;

import com.ssafy.tingbackend.chatting.dto.MessageRequestDto;
import com.ssafy.tingbackend.chatting.dto.StompDestination;
import com.ssafy.tingbackend.common.exception.CommonException;
import com.ssafy.tingbackend.common.exception.ExceptionType;
import com.ssafy.tingbackend.entity.chatting.ChattingUser;
import com.ssafy.tingbackend.friend.dto.ChattingMessageDto;
import com.ssafy.tingbackend.friend.repository.ChattingMessageRepository;
import com.ssafy.tingbackend.friend.repository.ChattingUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ChattingService {
    private final SimpMessagingTemplate template;
    private final ChattingMessageRepository chattingMessageRepository;
    private final ChattingUserRepository chattingUserRepository;
    private final StompDestination destination = new StompDestination();
    private final MessageRequestDto curMessage = new MessageRequestDto();

    @Transactional
    public void convertAndSendMessage(Long roomId, Long userId, String content) {
        ChattingMessageDto chattingMessageDto = new ChattingMessageDto(roomId, userId, content);
        chattingMessageRepository.save(chattingMessageDto);
        ChattingUser friendChattingUser =  chattingUserRepository.findFriendChattingUser(roomId, userId)
                .orElseThrow(() -> new CommonException(ExceptionType.CHATTING_USER_NOT_FOUND));
        template.convertAndSend("/subscription/list" + friendChattingUser.getUser().getId(), chattingMessageDto);
//        template.convertAndSend("/subscription/list", chattingMessageDto); // 테스트용
        template.convertAndSend("/subscription/chat/room/" + roomId, chattingMessageDto);
        friendChattingUser.setUnread(friendChattingUser.getUnread()+1);
    }

    @Transactional
    public void resetUnread(Long roomId, Long userId) {
        System.out.println("start==================");
        ChattingUser chattingUser = chattingUserRepository.findByRoomAndUser(roomId, userId)
                .orElseThrow(() -> new CommonException(ExceptionType.CHATTING_USER_NOT_FOUND));
        System.out.println("found=====================");
        chattingUser.setUnread(0);
    }

    public void enter(MessageRequestDto messageRequestDto) {
        destination.setLastDestination("list");
        destination.setRoomLast(false);
        curMessage.setUserId(messageRequestDto.getUserId());
        curMessage.setRoomId(messageRequestDto.getRoomId());
        resetUnread(messageRequestDto.getRoomId(), messageRequestDto.getUserId());
    }

    public void quit(MessageRequestDto messageRequestDto) {
        destination.setLastDestination("room");
        destination.setRoomLast(true);
        resetUnread(messageRequestDto.getRoomId(), messageRequestDto.getUserId());
    }

    public void checkAndReset() {
        if(destination.isRoomLast()) {
            resetUnread(curMessage.getRoomId(), curMessage.getUserId());
        }
    }
}