package org.myweb.jobis.jobposting.model.service;

import jakarta.persistence.PrePersist;
import lombok.RequiredArgsConstructor;
import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
import org.myweb.jobis.jobposting.jpa.repository.JobFavoritesRepository;
import org.myweb.jobis.jobposting.model.dto.JobFavorites;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobFavoritesService {

    private final JobFavoritesRepository jobFavoritesRepository;

    // 즐겨찾기 추가
    @Transactional
    public void addFavorite(JobFavorites jobFavorites) {
        String jobFavoritesNo = UUID.randomUUID().toString();

        boolean exists = jobFavoritesRepository.existsFavoriteByUuidAndJobPostingId(
                jobFavorites.getUuid(), jobFavorites.getJobPostingId()
        );
        if (exists) {
            throw new IllegalStateException("이미 즐겨찾기에 추가된 공고입니다.");
        }

        // 여기서 jobFavorites.getUuid()가 올바르게 전달되는지 확인
        if (jobFavorites.getUuid() == null || jobFavorites.getUuid().isEmpty()) {
            throw new IllegalStateException("UUID가 누락되었습니다.");
        }

        jobFavorites.setJobFavoritesNo(jobFavoritesNo);

        // Entity로 변환하여 저장
        JobFavoritesEntity entity = JobFavoritesEntity.fromDto(jobFavorites);
        jobFavoritesRepository.save(entity);
    }

    // 즐겨찾기 삭제
    @Transactional
    public void removeFavorite(String uuid, String jobPostingId) {
        try {
            System.out.println("즐겨찾기 삭제 요청: uuid=" + uuid + ", jobPostingId=" + jobPostingId);  // 로그 추가

            JobFavoritesEntity entity = jobFavoritesRepository.findFavoriteByUuidAndJobPostingId(uuid, jobPostingId);
            if (entity != null) {
                jobFavoritesRepository.delete(entity);
                System.out.println("즐겨찾기 삭제 완료: " + entity);  // 로그 추가
            } else {
                throw new IllegalStateException("해당 공고가 즐겨찾기에 없습니다.");
            }
        } catch (Exception e) {
            System.out.println("즐겨찾기 삭제 중 오류 발생: " + e.getMessage());  // 오류 메시지 출력
            e.printStackTrace();  // 예외 전체 스택 출력
            throw e;  // 예외를 다시 던져서 컨트롤러에서 처리
        }
    }

    // 즐겨찾기 목록 조회
    public List<JobFavorites> getFavorites(String uuid) {
        try {
            System.out.println("즐겨찾기 목록 조회 요청: uuid=" + uuid);  // 로그 추가
            List<JobFavorites> favorites = jobFavoritesRepository.findFavoritesByUuid(uuid)
                    .stream()
                    .map(JobFavoritesEntity::toDto)
                    .collect(Collectors.toList());
            System.out.println("즐겨찾기 목록 조회 완료: " + favorites);  // 로그 추가
            return favorites;
        } catch (Exception e) {
            System.out.println("즐겨찾기 목록 조회 중 오류 발생: " + e.getMessage());  // 오류 메시지 출력
            e.printStackTrace();  // 예외 전체 스택 출력
            throw e;  // 예외를 다시 던져서 컨트롤러에서 처리
        }
    }
}
