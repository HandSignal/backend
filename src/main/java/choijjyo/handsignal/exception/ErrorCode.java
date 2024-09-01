package choijjyo.handsignal.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 방이 존재하지 않습니다.", "올바른 방 ID를 사용하십시오."),
    ROOM_FULL(HttpStatus.FORBIDDEN, "방이 가득 찼습니다.", "다른 방을 생성하거나 참가해 주세요."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String solution;
}
