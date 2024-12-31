package org.myweb.jobis.jobposting.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import org.myweb.jobis.jobposting.model.service.JobFavoritesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class JobFavoritesController {

    private final JobFavoritesService jobFavoritesService;

    // 즐겨찾기 추가
    @PostMapping
    public ResponseEntity<JobFavorites> addFavorite(@RequestBody JobFavorites favoriteDto) {
        try {
            log.info("즐겨찾기 추가 요청: {}", favoriteDto); // 즐겨찾기 추가 요청 시 로그
            JobFavorites addedFavorite = jobFavoritesService.addFavorite(favoriteDto);
            log.info("즐겨찾기 추가 성공: {}", addedFavorite); // 성공적으로 추가된 즐겨찾기 로그
            return ResponseEntity.ok(addedFavorite);
        } catch (RuntimeException e) {
            log.error("즐겨찾기 추가 실패: {}", e.getMessage(), e); // 예외 발생 시 로그
            return ResponseEntity.status(400).body(null); // 예외 처리
        }
    }

    // 특정 사용자(UUID)의 즐겨찾기 목록 조회
    @GetMapping("/search")
    public ResponseEntity<List<JobFavorites>> getFavorites(@RequestParam String uuid) {
        try {
            log.info("사용자 UUID {}의 즐겨찾기 목록 조회 요청", uuid); // 즐겨찾기 목록 조회 요청 시 로그
            List<JobFavorites> favorites = jobFavoritesService.getFavorites(uuid);
            log.info("사용자 UUID {}의 즐겨찾기 목록 조회 성공: {}개", uuid, favorites.size()); // 즐겨찾기 목록 조회 성공 로그
            return ResponseEntity.ok(favorites);
        } catch (Exception e) {
            log.error("사용자 UUID {}의 즐겨찾기 목록 조회 실패: {}", uuid, e.getMessage(), e); // 예외 발생 시 로그
            return ResponseEntity.status(500).body(null); // 예외 처리
        }
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<Void> removeFavorite(@RequestParam String uuid, @RequestParam String jobPostingId) {
        try {
            log.info("사용자 {}의 채용공고 {} 즐겨찾기 삭제 요청", uuid, jobPostingId); // 즐겨찾기 삭제 요청 시 로그
            jobFavoritesService.removeFavorite(uuid, jobPostingId);
            log.info("사용자 {}의 채용공고 {} 즐겨찾기 삭제 성공", uuid, jobPostingId); // 삭제 성공 로그
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("사용자 {}의 채용공고 {} 즐겨찾기 삭제 실패: {}", uuid, jobPostingId, e.getMessage(), e); // 예외 발생 시 로그
            return ResponseEntity.status(500).build(); // 예외 처리
        }
    }

    // 즐겨찾기 여부 확인
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkFavorite(@RequestParam String uuid, @RequestParam String jobPostingId) {
        try {
            log.info("사용자 {}가 채용공고 {}를 즐겨찾기 했는지 확인 요청", uuid, jobPostingId); // 즐겨찾기 여부 확인 요청 시 로그
            boolean isFavorite = jobFavoritesService.isFavorite(uuid, jobPostingId);
            log.info("사용자 {}가 채용공고 {}를 즐겨찾기 했는지 확인: {}", uuid, jobPostingId, isFavorite); // 즐겨찾기 여부 확인 로그
            return ResponseEntity.ok(isFavorite);
        } catch (Exception e) {
            log.error("사용자 {}가 채용공고 {}를 즐겨찾기 했는지 확인 실패: {}", uuid, jobPostingId, e.getMessage(), e); // 예외 발생 시 로그
            return ResponseEntity.status(500).body(false); // 예외 처리
        }
    }
}
