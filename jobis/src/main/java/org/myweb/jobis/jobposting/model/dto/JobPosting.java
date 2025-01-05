package org.myweb.jobis.jobposting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPosting {

    private String id;
    private String url;
    private int active;
    private Company company;
    private Position position;
    private String keyword;
    private Salary salary;
    private String postingTimestamp;
    private String postingDate;
    private String modificationTimestamp;
    private String openingTimestamp;
    private String expirationTimestamp;
    private String expirationDate;
    private CloseType closeType;
    private int readCnt;
    private int applyCnt;

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
            private int code;
            private int min;
            private int max;
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
