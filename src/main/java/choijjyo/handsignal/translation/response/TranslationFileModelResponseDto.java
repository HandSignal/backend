package choijjyo.handsignal.translation.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TranslationFileModelResponseDto {
    private String name;
    private String url;
    private String modelResult;

    public TranslationFileModelResponseDto(String fileName, String fileUrl, String modelResult) {
        this.name = fileName;
        this.url = fileUrl;
        this.modelResult = modelResult;
    }
}
