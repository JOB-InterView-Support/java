package org.myweb.jobis.jobposting.controller;

import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import org.myweb.jobis.jobposting.model.service.JobFavoritesService;
import org.myweb.jobis.jobposting.model.service.JobPostingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class JobFavoritesController {

    private final JobFavoritesService jobFavoritesService;

    public JobFavoritesController(JobFavoritesService jobFavoritesService) {
        this.jobFavoritesService = jobFavoritesService;
    }

    // 즐겨찾기 추가
    @PostMapping()
    public ResponseEntity<JobFavorites> addFavorite(@RequestBody JobFavorites jobFavorites) {
        JobFavorites addedFavorite = jobFavoritesService.addFavorite(jobFavorites);
        return ResponseEntity.ok(addedFavorite);
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/{jobFavoritesNo}")
    public ResponseEntity<String> removeFavorite(@PathVariable String jobFavoritesNo) {
        jobFavoritesService.removeFavorite(jobFavoritesNo);
        return ResponseEntity.ok("삭제 완료");
    }

    // 즐겨찾기 조회
    @GetMapping("/{uuid}")
    public ResponseEntity<List<JobFavorites>> listFavorites(@PathVariable String uuid) {
        List<JobFavorites> favorites = jobFavoritesService.listFavorites(uuid);
        return ResponseEntity.ok(favorites);
    }
}
