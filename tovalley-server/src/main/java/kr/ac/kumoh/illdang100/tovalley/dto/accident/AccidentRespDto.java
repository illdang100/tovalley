package kr.ac.kumoh.illdang100.tovalley.dto.accident;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
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

        private int totalDeathCnt;
        private int totalDisappearanceCnt;
        private int totalInjuryCnt;
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

    @AllArgsConstructor
    @Data
    public static class AccidentForAdminByWaterPlace {

        private Page<AccidentDetailRespDto> accidentDetail;

        private int totalDeathCnt;
        private int totalDisappearanceCnt;
        private int totalInjuryCnt;
    }

    @AllArgsConstructor
    @Data
    public static class AccidentDetailRespDto {

        private Long accidentId;
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate accidentDate;
        private String accidentCondition;
        private Integer peopleNum;

        public AccidentDetailRespDto(Long accidentId, LocalDate accidentDate, AccidentEnum accidentCondition, Integer peopleNum) {
            this.accidentId = accidentId;
            this.accidentDate = accidentDate;
            this.accidentCondition = accidentCondition.getValue();
            this.peopleNum = peopleNum;
        }
    }
}
