package kr.ac.kumoh.illdang100.tovalley.dto.trip_schedule;

import kr.ac.kumoh.illdang100.tovalley.dto.YearMonthRange;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
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

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class AddTripScheduleReqDto {

        @NotNull
        private Long waterPlaceId;
        @NotNull
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private LocalDate tripDate;
        @NotNull
        @Min(0)
        private Integer tripPartySize;
    }
}
