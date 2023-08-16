package kr.ac.kumoh.illdang100.tovalley.dto.trip_schedule;

import kr.ac.kumoh.illdang100.tovalley.dto.YearMonthRange;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.YearMonth;

public class TripScheduleReqDto {

    @AllArgsConstructor
    @Data
    public static class RetrieveTripAttendeesPerMonth {

        @NotNull
        @YearMonthRange(message = "날짜 범위가 잘못 지정되었습니다")
        @DateTimeFormat(pattern = "yyyy-MM")
        private YearMonth yearMonth;
    }
}
