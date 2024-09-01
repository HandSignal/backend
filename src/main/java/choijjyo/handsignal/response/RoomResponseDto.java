package choijjyo.handsignal.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomResponseDto {
    private String roomId;
    private String entryUrl;

    public RoomResponseDto(String roomId, String entryUrl) {
        this.roomId = roomId;
        this.entryUrl = entryUrl;
    }
}
