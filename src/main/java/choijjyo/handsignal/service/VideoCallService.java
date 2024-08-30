package choijjyo.handsignal.service;

import choijjyo.handsignal.entity.ChatRoom;
import choijjyo.handsignal.entity.UserEntry;
import choijjyo.handsignal.repository.ChatRoomRepository;
import choijjyo.handsignal.repository.UserEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideoCallService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserEntryRepository userEntryRepository;

    public ChatRoom createRoom() {
        ChatRoom chatRoom = new ChatRoom();
        return chatRoomRepository.save(chatRoom);
    }

    public boolean enterRoom(String roomId, String userName, Instant timestamp) {
        Optional<ChatRoom> optionalRoom = chatRoomRepository.findById(roomId);

        if (optionalRoom.isPresent()) {
            ChatRoom chatRoom = optionalRoom.get();

            // 방이 꽉 찬 상태인지 확인
            if (chatRoom.isFull()) {
                return false;
            }

            UserEntry userEntry = new UserEntry();
            userEntry.setUserName(userName);
            userEntry.setTimestamp(timestamp);
            userEntry.setChatRoom(chatRoom);

            userEntryRepository.save(userEntry);

            // 사용자가 두 명이 되면 방을 꽉 찬 상태로 설정
            if (chatRoom.getEntries().size() >= 2) {
                chatRoom.setFull(true);
                chatRoomRepository.save(chatRoom);  // 상태 업데이트
            }

            return true;
        }

        return false;
    }
}
