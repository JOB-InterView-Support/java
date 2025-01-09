//package org.myweb.jobis.jobposting.jpa.repository;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.TypedQuery;
//import org.myweb.jobis.jobposting.jpa.entity.JobFavoritesEntity;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Repository
//public class JobFavoritesRepositoryCustomImpl implements JobFavoritesRepositoryCustom {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Override
//    public List<JobFavoritesEntity> findFavoritesByUuid(String uuid) {
//        return entityManager.createQuery(
//                        "SELECT f FROM JobFavoritesEntity f WHERE f.uuid = :uuid", JobFavoritesEntity.class)
//                .setParameter("uuid", uuid)
//                .getResultList();
//    }
//
//    @Override
//    @Transactional
//    public void deleteFavoriteByUuidAndJobPostingId(String uuid, String jobPostingId) {
//        entityManager.createQuery(
//                        "DELETE FROM JobFavoritesEntity f WHERE f.uuid = :uuid AND f.jobPostingId = :jobPostingId")
//                .setParameter("uuid", uuid)
//                .setParameter("jobPostingId", jobPostingId)
//                .executeUpdate();
//    }
//}