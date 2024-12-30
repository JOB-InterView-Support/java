package org.myweb.jobis.jobposting.controller;

import lombok.RequiredArgsConstructor;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import org.myweb.jobis.jobposting.model.service.JobFavoritesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class JobFavoritesController {

    private final JobFavoritesService jobFavoritesService;

    // 즐겨찾기 추가
    @PostMapping()
    public ResponseEntity<?> insertFavorite(@RequestBody JobFavorites jobFavorites) {
        if (jobFavorites.getUuid() == null || jobFavorites.getUuid().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UUID가 누락되었습니다.");
        }

        try {
            // JOB_FAVORITES_NO 생성 (UUID를 이용해 고유한 값 생성)
            String jobFavoritesNo = UUID.randomUUID().toString();  // UUID로 고유 ID 생성
            jobFavorites.setJobFavoritesNo(jobFavoritesNo);  // 생성된 JOB_FAVORITES_NO를 설정

            // 즐겨찾기 추가
            jobFavoritesService.addFavorite(jobFavorites);
            return ResponseEntity.ok("즐겨찾기에 추가되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("즐겨찾기 추가 실패: " + e.getMessage());
        }
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFavorite(@RequestParam String uuid, @RequestParam String jobPostingId) {
        try {
            System.out.println("즐겨찾기 삭제 요청: uuid=" + uuid + ", jobPostingId=" + jobPostingId);  // 로그 추가
            jobFavoritesService.removeFavorite(uuid, jobPostingId);
            return ResponseEntity.ok("즐겨찾기에서 삭제되었습니다.");
        } catch (Exception e) {
            System.out.println("즐겨찾기 삭제 실패: " + e.getMessage());  // 오류 메시지 출력
            e.printStackTrace();  // 예외 전체 스택 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("즐겨찾기 삭제 실패: " + e.getMessage());
        }
    }

    // 즐겨찾기 목록 조회
    @GetMapping("/search")
    public ResponseEntity<List<JobFavorites>> getFavorites(@RequestParam String uuid) {
        try {
            System.out.println("즐겨찾기 목록 조회 요청: uuid=" + uuid);  // 로그 추가
            List<JobFavorites> favorites = jobFavoritesService.getFavorites(uuid);
            return ResponseEntity.ok(favorites);
        } catch (Exception e) {
            System.out.println("즐겨찾기 목록 조회 실패: " + e.getMessage());  // 오류 메시지 출력
            e.printStackTrace();  // 예외 전체 스택 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
