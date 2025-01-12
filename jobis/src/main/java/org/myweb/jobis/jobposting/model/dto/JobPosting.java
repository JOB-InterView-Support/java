package org.myweb.jobis.jobposting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JobPosting {
    private String title;  // 직무명
    private String location;  // 직무 위치
    private String url;
    private Integer active;
    private Company company;
    private Position position;
    private String keyword;
    private Salary salary;
    private String id;
    private String postingTimestamp;
    private String postingDate;
    private String modificationTimestamp;
    private String openingTimestamp;
    private String expirationTimestamp;
    private String expirationDate;
    private CloseType closeType;
    private Integer readCnt;
    private Integer applyCnt;


    @Data
    public static class Company {
        private Detail detail;

        @Data
        public static class Detail {
            private String href;
            private String name;
        }
    }

    @Data
    public static class Position {
        private String title;
        private Industry industry;
        private Location location;
        private JobType jobType;
        private JobMidCode jobMidCode;
        private JobCode jobCode;
        private ExperienceLevel experienceLevel;
        private RequiredEducationLevel requiredEducationLevel;

        @Data
        public static class Industry {
            private String code;
            private String name;
        }

        @Data
        public static class Location {
            private String code;
            private String name;
        }

        @Data
        public static class JobType {
            private String code;
            private String name;
        }

        @Data
        public static class JobMidCode {
            private String code;
            private String name;
        }

        @Data
        public static class JobCode {
            private String code;
            private String name;
        }

        @Data
        public static class ExperienceLevel {
            private String code;
            private Integer min;
            private Integer max;
            private String name;
        }

        @Data
        public static class RequiredEducationLevel {
            private String code;
            private String name;
        }
    }

    @Data
    public static class Salary {
        private String code;
        private String name;
    }

    @Data
    public static class CloseType {
        private String code;
        private String name;
    }
}
