package choijjyo.handsignal.videoCall.controller;

import choijjyo.handsignal.common.dto.NormalResponseDto;
import choijjyo.handsignal.exception.HandSignalException;
import choijjyo.handsignal.videoCall.response.RoomResponseDto;
import choijjyo.handsignal.videoCall.service.VideoCallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/video-calls/room")
@RequiredArgsConstructor
public class VideoCallController {

    private final VideoCallService videoCallService;

    @PostMapping("/create")
    public ResponseEntity<RoomResponseDto> createRoom() {
        var chatRoom = videoCallService.createRoom();
        String entryUrl = "/video-calls/room/entry/" + chatRoom.getId();

        RoomResponseDto response = new RoomResponseDto(chatRoom.getId(), entryUrl);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/entry/{roomId}")
    public ResponseEntity<NormalResponseDto> enterRoom(
            @PathVariable String roomId,
            @RequestParam(required = false) String name) {

        if (name == null || name.isEmpty()) {
            name = "User" + UUID.randomUUID().toString().substring(0, 8); // 랜덤 이름 생성
        }

        Instant timestamp = Instant.now();

        try {
            videoCallService.enterRoom(roomId, name, timestamp);
            return ResponseEntity.ok(NormalResponseDto.success());
        } catch (HandSignalException e) {
            NormalResponseDto errorResponse = NormalResponseDto.fail();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(errorResponse);
        }
    }
}