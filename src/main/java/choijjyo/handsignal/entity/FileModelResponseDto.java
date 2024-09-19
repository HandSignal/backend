package choijjyo.handsignal.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileModelResponseDto {
    private String name;
    private String url;
    private String modelResult;

    public FileModelResponseDto(String fileName, String fileUrl, String modelResult) {
        this.name = fileName;
        this.url = fileUrl;
        this.modelResult = modelResult;
    }
}
