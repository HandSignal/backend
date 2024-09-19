package choijjyo.handsignal.translation.controller;

import choijjyo.handsignal.common.dto.NormalResponseDto;
import choijjyo.handsignal.translation.entity.TranslationFileRecord;
import choijjyo.handsignal.exception.HandSignalException;
import choijjyo.handsignal.translation.repository.TranslationFileRecordRepository;
import choijjyo.handsignal.translation.service.TranslationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/translate")
@RequiredArgsConstructor
@Tag(name = "Translation", description = "수화를 텍스트로 번역하고 관련 파일 작업을 처리")
public class TranslationController {

    private final TranslationService translationService;
    private final TranslationFileRecordRepository translationFileRecordRepository;
    private final RestTemplate restTemplate; // 추가된 부분

    @Value("${flask.api.url}") // Flask API URL 설정
    private String flaskApiUrl;

    @Operation(
            summary = "JSON 파일을 S3에 업로드하고, Flask 예측 모델을 호출하여 손 좌표를 예측합니다.",
            description = "업로드된 파일은 다음과 같은 구조의 JSON 형식이어야 합니다:\n" +
                    "{\n" +
                    "  \"pose_keypoint\": [ /* List of pose keypoints */ ],\n" +
                    "  \"left_hand_keypoint\": [ /* List of left hand keypoints */ ],\n" +
                    "  \"right_hand_keypoint\": [ /* List of right hand keypoints */ ]\n" +
                    "}\n" +
                    "각 키포인트에는 'x', 'y', 'z', 'visibility' 필드가 있어야 합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "파일 업로드 성공"),
            @ApiResponse(responseCode = "500", description = "파일 업로드 실패")
    })
    @PostMapping(value = "/sign-language", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NormalResponseDto> uploadFile(@RequestParam("data") MultipartFile file) {
        try {
            // 파일을 S3에 업로드
            String fileUrl = translationService.uploadFile(file);

            // 파일 정보를 데이터베이스에 저장
            TranslationFileRecord translationFileRecord = new TranslationFileRecord();
            translationFileRecord.setFileName(file.getOriginalFilename());
            translationFileRecord.setFileUrl(fileUrl);
            translationFileRecord.setFileSize(file.getSize());
            translationFileRecord.setUploadedAt(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault())
                    .format(Instant.now()));

            translationFileRecordRepository.save(translationFileRecord);

            // Flask API 호출
            String fileName = file.getOriginalFilename();
            String apiUrl = flaskApiUrl + "/predict";

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            String jsonBody = String.format("{\"s3url\": \"%s\"}", fileUrl.replace("\"", "\\\""));
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            String result = restTemplate.postForObject(apiUrl, requestEntity, String.class);

            NormalResponseDto response = NormalResponseDto.success();
            response.setMessage("File uploaded successfully");
            response.setFileModelResult(fileName, fileUrl, result.replace("\"", ""));
            return ResponseEntity.ok(response);

        } catch (HandSignalException e) {
            NormalResponseDto errorResponse = NormalResponseDto.fail();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(errorResponse);

        } catch (IOException e) {
            NormalResponseDto errorResponse = NormalResponseDto.fail();
            errorResponse.setMessage("Failed to upload file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
