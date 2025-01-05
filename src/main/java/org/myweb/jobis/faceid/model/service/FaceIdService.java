package org.myweb.jobis.faceid.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.faceid.jpa.entity.FaceIdEntity;
import org.myweb.jobis.faceid.jpa.repository.FaceIdRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FaceIdService {

    private final FaceIdRepository faceIdRepository;



    public void deleteImageAndDataByUuid(String uuid) {
        // 1. FACEID 테이블에서 데이터 조회
        FaceIdEntity faceId = faceIdRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("No FaceId record found for UUID: " + uuid));

        String imagePath = faceId.getImagePath();
        if (imagePath == null || imagePath.isEmpty()) {
            throw new IllegalStateException("No IMAGE_PATH found for UUID: " + uuid);
        }

        // 2. 파일 삭제
        Path filePath = Paths.get(imagePath);
        try {
            Files.deleteIfExists(filePath); // 파일이 존재하면 삭제
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file at path: " + imagePath, e);
        }

        // 3. FACEID 테이블에서 데이터 삭제
        faceIdRepository.delete(faceId);
    }
}
