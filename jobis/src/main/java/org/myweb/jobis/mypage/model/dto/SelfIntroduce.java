package org.myweb.jobis.mypage.model.dto;

import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myweb.jobis.mypage.jpa.entity.SelfIntroduceEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelfIntroduce {

    private String introNo; // 자기소개서 번호
    private String uuid; // 사용자 UUID
    private String introTitle; // 자기소개서 제목
    private String introContents; // 자기소개서 내용
    private LocalDateTime introDate; // 자기소개서 작성일자
    private String applicationCompanyName; // 지원 회사명
    private String workType; // 지원 직무
    private String certificate; // 보유 자격증
    private String introIsDeleted; // 자기소개서 삭제 여부
    private LocalDateTime introDeletedDate; // 자기소개서 삭제 일자
    private String introIsEdited;


    /**
     * DTO -> Entity 변환 메서드
     *
     * @return SelfIntroduceEntity
     */
    public SelfIntroduceEntity toEntity() {
        return SelfIntroduceEntity.builder()
                .introNo(this.introNo)
                .uuid(this.uuid)
                .introTitle(this.introTitle)
                .introContents(this.introContents)
                .introDate(this.introDate)
                .applicationCompanyName(this.applicationCompanyName)
                .workType(this.workType)
                .certificate(this.certificate)
                .introIsDeleted(this.introIsDeleted)
                .introDeletedDate(this.introDeletedDate)
                .introIsEdited(this.introIsEdited)
                .build();
    }

}
