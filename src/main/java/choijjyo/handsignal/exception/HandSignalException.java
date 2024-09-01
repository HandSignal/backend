package choijjyo.handsignal.exception;

import lombok.Getter;

@Getter
public class HandSignalException extends RuntimeException {
    private int status;
    private String message;
    private String solution;

    public HandSignalException(ErrorCode errorCode, String message) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getHttpStatus().value();
        this.solution = errorCode.getSolution();
    }

    public HandSignalException(ErrorCode errorCode, String message, String solution) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getHttpStatus().value();
        this.solution = errorCode.getSolution();
    }

    public HandSignalException(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getHttpStatus().value();
        this.solution = errorCode.getSolution();
    }
}
