package choijjyo.handsignal.common.dto;

import choijjyo.handsignal.translation.response.TranslationFileModelResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NormalResponseDto {
    private String status;
    private String message;
    private TranslationFileModelResponseDto file;

    protected NormalResponseDto(String status) {
        this.status = status;
    }

    public static NormalResponseDto success() {
        return new NormalResponseDto("SUCCESS");
    }

    public static NormalResponseDto fail() {
        return new NormalResponseDto("FAIL");
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFileModelResult(String fileName, String fileUrl, String modelResult) {
        this.file = new TranslationFileModelResponseDto(fileName, fileUrl, modelResult);
    }
}
