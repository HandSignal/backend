package choijjyo.handsignal.videoCall.service;

import choijjyo.handsignal.videoCall.entity.ChatRoom;
import choijjyo.handsignal.videoCall.entity.UserEntry;
import choijjyo.handsignal.exception.ErrorCode;
import choijjyo.handsignal.exception.HandSignalException;
import choijjyo.handsignal.videoCall.repository.ChatRoomRepository;
import choijjyo.handsignal.videoCall.repository.UserEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class VideoCallService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserEntryRepository userEntryRepository;

    public ChatRoom createRoom() {
        ChatRoom chatRoom = new ChatRoom();
        return chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void enterRoom(String roomId, String userName, Instant timestamp) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new HandSignalException(ErrorCode.ROOM_NOT_FOUND));

        if (chatRoom.isFull()) {
            throw new HandSignalException(ErrorCode.ROOM_FULL);
        }

        UserEntry userEntry = new UserEntry();
        userEntry.setUserName(userName);
        userEntry.setTimestamp(timestamp);
        userEntry.setChatRoom(chatRoom);

        userEntryRepository.save(userEntry);

        // 방에 두 명이 되면 방을 꽉 찼다고 표시
        if (chatRoom.getEntries().size() >= 2) {
            chatRoom.setFull(true);
            chatRoomRepository.save(chatRoom);  // 방 상태 업데이트
        }
    }
}
