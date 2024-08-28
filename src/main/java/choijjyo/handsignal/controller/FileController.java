package choijjyo.handsignal.controller;

import choijjyo.handsignal.entity.FileRecord;
import choijjyo.handsignal.repository.FileRecordRepository;
import choijjyo.handsignal.service.S3Service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
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

            return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload file: " + e.getMessage();
        }
    }
}
