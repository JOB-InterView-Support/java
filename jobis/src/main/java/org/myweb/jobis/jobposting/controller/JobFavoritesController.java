package org.myweb.jobis.jobposting.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import org.myweb.jobis.jobposting.model.service.JobFavoritesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class JobFavoritesController {

    private final JobFavoritesService jobFavoritesService;

    // 즐겨찾기 추가
    @PostMapping()
    public ResponseEntity<JobFavorites> addFavorite(@RequestBody JobFavorites favoriteDto) {
        try {
            JobFavorites addedFavorite = jobFavoritesService.addFavorite(favoriteDto);
            return ResponseEntity.ok(addedFavorite);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(null); // 예외 처리 필요
        }
    }

    // 특정 사용자(UUID)의 즐겨찾기 목록 조회
    @GetMapping("/search")
    public ResponseEntity<List<JobFavorites>> getFavorites(@RequestParam String uuid) {
        try {
            List<JobFavorites> favorites = jobFavoritesService.getFavorites(uuid);
            return ResponseEntity.ok(favorites);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // 예외 처리 필요
        }
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<Void> removeFavorite(@RequestParam String uuid, @RequestParam String jobPostingId) {
        try {
            jobFavoritesService.removeFavorite(uuid, jobPostingId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // 예외 처리 필요
        }
    }

    // 즐겨찾기 여부 확인
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkFavorite(@RequestParam String uuid, @RequestParam String jobPostingId) {
        try {
            boolean isFavorite = jobFavoritesService.isFavorite(uuid, jobPostingId);
            return ResponseEntity.ok(isFavorite);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false); // 예외 처리 필요
        }
    }
}
