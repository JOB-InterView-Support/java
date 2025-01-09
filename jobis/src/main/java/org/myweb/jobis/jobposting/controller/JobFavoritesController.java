package org.myweb.jobis.jobposting.controller;

import org.myweb.jobis.jobposting.model.dto.JobFavoriteRequest;
import org.myweb.jobis.jobposting.model.dto.JobFavoriteResponse;
import org.myweb.jobis.jobposting.model.service.JobFavoritesService;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<Void> addFavorite(@RequestBody JobFavoriteRequest request) {
        jobFavoritesService.addFavorite(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobFavoriteResponse>> getFavorites(@RequestParam String uuid) {
        List<JobFavoriteResponse> favorites = jobFavoritesService.getFavorites(uuid);
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFavorite(@RequestBody JobFavoriteRequest request) {
        jobFavoritesService.deleteFavorite(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkFavorite(
            @RequestParam String uuid,
            @RequestParam String jobPostingId) {
        boolean isFavorite = jobFavoritesService.isFavorite(uuid, jobPostingId);
        return ResponseEntity.ok(isFavorite);
    }
}
