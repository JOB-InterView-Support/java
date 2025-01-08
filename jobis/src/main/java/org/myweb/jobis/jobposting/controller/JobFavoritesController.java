package org.myweb.jobis.jobposting.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import org.myweb.jobis.jobposting.model.service.JobFavoritesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
@Slf4j
public class JobFavoritesController {

    private final JobFavoritesService jobFavoritesService;

    @PostMapping
    public ResponseEntity<JobFavorites> addFavorite(@RequestBody JobFavorites jobFavorites) {
        JobFavorites addedFavorite = jobFavoritesService.addFavorite(jobFavorites.getUuid(), jobFavorites.getJobPostingId());
        return ResponseEntity.ok(addedFavorite);
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobFavorites>> getFavoritesWithDetails(@RequestParam String uuid) {
        List<JobFavorites> favorites = jobFavoritesService.getFavoritesWithDetails(uuid);
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> removeFavorite(@RequestParam String jobFavoritesNo) {
        jobFavoritesService.removeFavorite(jobFavoritesNo);
        return ResponseEntity.noContent().build();
    }
}
