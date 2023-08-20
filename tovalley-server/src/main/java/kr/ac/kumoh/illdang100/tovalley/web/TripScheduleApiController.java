package kr.ac.kumoh.illdang100.tovalley.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.service.trip_schedule.TripScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Map;

import static kr.ac.kumoh.illdang100.tovalley.dto.trip_schedule.TripScheduleReqDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
@Tag(name = "TripSchedule", description = "여행계획 API Document")
public class TripScheduleApiController {

    private final TripScheduleService tripScheduleService;

    @GetMapping("/auth/water-places/{id}/trip-schedules")
    @Operation(summary = "물놀이 장소 날짜별 여행객 수 조회", description = "물놀이 장소 날짜별 여행객 수를 반환합니다.")
    public ResponseEntity<?> getTripAttendeesByWaterPlace(@PathVariable("id") Long waterPlaceId,
                                                          @ModelAttribute @Valid RetrieveTripAttendeesPerMonth retrieveTripAttendeesPerMonth,
                                                          BindingResult bindingResult) {

        Map<LocalDate, Integer> result =
                tripScheduleService.getTripAttendeesByWaterPlace(waterPlaceId, retrieveTripAttendeesPerMonth.getYearMonth());

        return new ResponseEntity<>(new ResponseDto<>(1, "날짜별 여행객 수 조회에 성공하였습니다", result), HttpStatus.OK);
    }
}
