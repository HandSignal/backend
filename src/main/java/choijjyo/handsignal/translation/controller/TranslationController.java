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
import java.util.Optional;

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
            summary = "JSON 파일을 S3에 업로드하고, Flask 예측 모델을 호출하여 수화를 번역",
            description = "업로드된 파일은 다음과 같은 구조의 JSON 형식이어야 합니다:\n\n" +
                    "{\n\n" +
                    "\t\"pose_keypoint\": [ /* List of pose keypoints */ ],\n\n" +
                    "\t\"left_hand_keypoint\": [ /* List of left hand keypoints */ ],\n\n" +
                    "\t\"right_hand_keypoint\": [ /* List of right hand keypoints */ ]\n\n" +
                    "}\n\n" +
                    "각 키값에는 'x', 'y', 'z', 'visibility' 필드가 있어야 합니다."
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

            // Flask API 호출
            String apiUrl = flaskApiUrl + "/predict";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            String jsonBody = String.format("{\"s3url\": \"%s\"}", fileUrl.replace("\"", "\\\""));
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            String result = restTemplate.postForObject(apiUrl, requestEntity, String.class);

            // 파일 정보를 데이터베이스에 저장
            TranslationFileRecord translationFileRecord = new TranslationFileRecord();
            translationFileRecord.setFileName(file.getOriginalFilename());
            translationFileRecord.setFileUrl(fileUrl);
            translationFileRecord.setFileSize(file.getSize());
            translationFileRecord.setUploadedAt(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault())
                    .format(Instant.now()));
            translationFileRecord.setModelResult(result.replace("\"", "")); // 결과 저장

            translationFileRecordRepository.save(translationFileRecord);

            NormalResponseDto response = NormalResponseDto.success();
            response.setMessage("File uploaded successfully");
            response.setFileModelResult(file.getOriginalFilename(), fileUrl, result.replace("\"", ""));
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

    @Operation(
            summary = "ID별 업로드 파일 기록 조회",
            description = "제공된 ID를 기준으로 데이터베이스에서 파일 기록을 가져옵니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "파일 기록 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "파일 기록 조회 실패")
            }
    )
    @GetMapping("/file-record/{id}")
    public ResponseEntity<TranslationFileRecord> getFileRecordById(@PathVariable Long id) {
        Optional<TranslationFileRecord> fileRecordOptional = translationFileRecordRepository.findById(id);
        if (fileRecordOptional.isPresent()) {
            return ResponseEntity.ok(fileRecordOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "모든 업로드 파일 기록 조회",
            description = "데이터베이스에서 모든 파일 기록을 가져옵니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "전체 파일 기록 조회 성공")
            }
    )
    @GetMapping("/file-records")
    public ResponseEntity<Iterable<TranslationFileRecord>> getAllFileRecords() {
        Iterable<TranslationFileRecord> fileRecords = translationFileRecordRepository.findAll();
        return ResponseEntity.ok(fileRecords);
    }
}
