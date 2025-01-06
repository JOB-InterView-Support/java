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

    @PostMapping
    public ResponseEntity<JobFavorites> addFavorite(@RequestBody JobFavorites jobFavorites) {
        return ResponseEntity.ok(jobFavoritesService.addFavorite(jobFavorites));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFavorite(
            @RequestParam String uuid,
            @RequestParam String jobFavoritesNo) {
        jobFavoritesService.deleteFavorite(uuid, jobFavoritesNo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobFavorites>> getFavorites(@RequestParam String uuid) {
        return ResponseEntity.ok(jobFavoritesService.getFavorites(uuid));
    }
}