package kr.ac.kumoh.illdang100.tovalley.dto.accident;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public class AccidentRespDto {

    @AllArgsConstructor
    @Data
    public static class WaterPlaceAccidentFor5YearsDto {

        private Integer totalDeathCnt;
        private Integer totalDisappearanceCnt;
        private Integer totalInjuryCnt;
    }

    @AllArgsConstructor
    @Data
    public static class AccidentCountDto {

        private List<AccidentCountPerMonthDto> accidentCountPerMonth;

        private String province;

        private Integer totalDeathCnt;
        private Integer totalDisappearanceCnt;
        private Integer totalInjuryCnt;
    }

    @AllArgsConstructor
    @Data
    public static class AccidentCountPerMonthDto {

        private Integer month;
        private Integer deathCnt;
        private Integer disappearanceCnt;
        private Integer injuryCnt;

        public void incrementDeathCnt(int cnt) {
            this.deathCnt = deathCnt + cnt;
        }

        public void incrementDisappearanceCnt(int cnt) {
            this.disappearanceCnt = disappearanceCnt + cnt;
        }

        public void incrementInjuryCnt(int cnt) {
            this.injuryCnt = injuryCnt + cnt;
        }
    }
}
