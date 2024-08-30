package choijjyo.handsignal.controller;

import choijjyo.handsignal.entity.ChatRoom;
import choijjyo.handsignal.service.VideoCallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/video-calls/room")
@RequiredArgsConstructor
public class VideoCallController {

    private final VideoCallService videoCallService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createRoom() {
        ChatRoom chatRoom = videoCallService.createRoom();
        String entryUrl = "/video-calls/room/entry/" + chatRoom.getId();

        Map<String, String> response = new HashMap<>();
        response.put("roomId", chatRoom.getId());
        response.put("entryUrl", entryUrl);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/entry/{roomId}")
    public ResponseEntity<Map<String, Object>> enterRoom(
            @PathVariable String roomId,
            @RequestParam(required = false) String name) {

        if (name == null || name.isEmpty()) {
            name = "User" + UUID.randomUUID().toString().substring(0, 8); // 랜덤 이름 생성
        }

        Instant timestamp = Instant.now();
        boolean success = videoCallService.enterRoom(roomId, name, timestamp);

        if (!success) {
            return ResponseEntity.status(403).body(Map.of("error", "Room is full or does not exist"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("roomId", roomId);
        response.put("userName", name);
        response.put("timestamp", timestamp.toString());

        return ResponseEntity.ok(response);
    }
}