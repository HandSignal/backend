package choijjyo.handsignal.controller;

import choijjyo.handsignal.entity.FileRecord;
import choijjyo.handsignal.repository.FileRecordRepository;
import choijjyo.handsignal.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final S3Service s3Service;
    private final FileRecordRepository fileRecordRepository;
    private final RestTemplate restTemplate; // 추가된 부분

    @Value("${flask.api.url}") // Flask API URL 설정
    private String flaskApiUrl;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("data") MultipartFile file) {
        try {
            // 파일을 S3에 업로드
            String fileUrl = s3Service.uploadFile(file);

            // 파일 정보를 데이터베이스에 저장
            FileRecord fileRecord = new FileRecord();
            fileRecord.setFileName(file.getOriginalFilename());
            fileRecord.setFileUrl(fileUrl);
            fileRecord.setFileSize(file.getSize());
            fileRecord.setUploadedAt(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault())
                    .format(Instant.now()));

            fileRecordRepository.save(fileRecord);

            // Flask API 호출
            String fileName = file.getOriginalFilename();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> requestEntity = new HttpEntity<>("{\"file_url\":\"" + fileUrl + "\"}", headers);
            ResponseEntity<String> response = restTemplate.exchange(flaskApiUrl + "/predict", HttpMethod.POST, requestEntity, String.class);

            return "File uploaded successfully: " + file.getOriginalFilename() + " - Flask Response: " + response.getBody();
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload file: " + e.getMessage();
        }
    }
}
