package kr.ac.kumoh.illdang100.tovalley.dto.accident;

import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public class AccidentReqDto {

    @AllArgsConstructor
    @Data
    public static class RetrieveAccidentCondition {

        @PastOrPresent
        private LocalDate accidentDate; // null이면 전체 날짜 조회
        private AccidentEnum accidentCond; // null이면 전체 날짜 조회
    }
}
