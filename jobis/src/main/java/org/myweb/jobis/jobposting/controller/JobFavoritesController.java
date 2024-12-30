package org.myweb.jobis.jobposting.controller;

import lombok.RequiredArgsConstructor;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import org.myweb.jobis.jobposting.model.service.JobFavoritesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class JobFavoritesController {

    private final JobFavoritesService jobFavoritesService;

    @PostMapping("/insert")
    public ResponseEntity<?> insertFavorite(@RequestBody JobFavorites jobFavorites) {
        try {
            jobFavoritesService.addFavorite(jobFavorites);
            return ResponseEntity.ok("즐겨찾기에 추가되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("즐겨찾기 추가 실패: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFavorite(@RequestParam String uuid, @RequestParam String jobPostingId) {
        try {
            jobFavoritesService.removeFavorite(uuid, jobPostingId);
            return ResponseEntity.ok("즐겨찾기에서 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("즐겨찾기 삭제 실패: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobFavorites>> getFavorites(@RequestParam String uuid) {
        try {
            return ResponseEntity.ok(jobFavoritesService.getFavorites(uuid));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
