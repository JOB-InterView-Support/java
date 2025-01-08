package org.myweb.jobis.jobposting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPostingResponse {

        private Jobs jobs = new Jobs(); // 기본 값 설정

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Jobs {
                private int count;
                private int start;
                private String total;
                private List<Job> job; // 기본 값 설정
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Job {
                private String url;
                private int active;
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
                private int readCnt;
                private int applyCnt;
                private boolean isFavorite; // 즐겨찾기 여부

                public void setIsFavorite(boolean contains) {
                        this.isFavorite = contains;
                }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Company {
                private Detail detail;

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class Detail {
                        private String href;
                        private String name;
                }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
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
                @NoArgsConstructor
                @AllArgsConstructor
                public static class Industry {
                        private String code;
                        private String name;
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class Location {
                        private String code;
                        private String name;
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class JobType {
                        private String code;
                        private String name;
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class JobMidCode {
                        private String code;
                        private String name;
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class JobCode {
                        private String code;
                        private String name;
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class ExperienceLevel {
                        private int code;
                        private int min;
                        private int max;
                        private String name;
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class RequiredEducationLevel {
                        private String code;
                        private String name;
                }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Salary {
                private String code;
                private String name;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class CloseType {
                private String code;
                private String name;
        }
}
