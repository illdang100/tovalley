package kr.ac.kumoh.illdang100.tovalley.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.service.trip_schedule.TripScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static kr.ac.kumoh.illdang100.tovalley.dto.trip_schedule.TripScheduleReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.trip_schedule.TripScheduleRespDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
@Tag(name = "TripSchedule", description = "여행계획 API Document")
public class TripScheduleApiController {

    private final TripScheduleService tripScheduleService;

    @PostMapping("/trip-schedules")
    @Operation(summary = "여행 일정 추가", description = "사용자의 여행 일정을 추가합니다.")
    public ResponseEntity<?> addTripSchedule(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                             @RequestBody @Valid AddTripScheduleReqDto addTripScheduleReqDto,
                                             BindingResult bindingResult) {

        Map<LocalDate, Integer> result =
                tripScheduleService.addTripSchedule(principalDetails.getMember().getId(), addTripScheduleReqDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "여행 일정이 성공적으로 추가되었습니다", result), HttpStatus.CREATED);
    }

    @DeleteMapping("/trip-schedules")
    @Operation(summary = "사용자의 앞으로의 일정 삭제", description = "사용자의 앞으로의 일정 정보를 삭제합니다.")
    public ResponseEntity<?> deleteTripSchedules(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                 @RequestParam List<Long> tripScheduleIds) {

        tripScheduleService.deleteTripSchedules(1L, tripScheduleIds);

        return new ResponseEntity<>(new ResponseDto<>(1, "일정 삭제가 성공적으로 완료되었습니다", null), HttpStatus.OK);
    }

    @GetMapping("/water-places/{id}/trip-schedules")
    @Operation(summary = "물놀이 장소 날짜별 여행객 수 조회", description = "물놀이 장소 날짜별 여행객 수를 반환합니다.")
    public ResponseEntity<?> getTripAttendeesByWaterPlace(@PathVariable("id") Long waterPlaceId,
                                                          @ModelAttribute @Valid RetrieveTripAttendeesPerMonth retrieveTripAttendeesPerMonth,
                                                          BindingResult bindingResult) {

        Map<LocalDate, Integer> result =
                tripScheduleService.getTripAttendeesByWaterPlace(waterPlaceId, retrieveTripAttendeesPerMonth.getYearMonth());

        return new ResponseEntity<>(new ResponseDto<>(1, "날짜별 여행객 수 조회에 성공하였습니다", result), HttpStatus.OK);
    }

    @GetMapping("/my-page/upcoming-schedules")
    @Operation(summary = "사용자의 앞으로의 일정 조회", description = "사용자의 앞으로의 일정 정보를 반환합니다.")
    public ResponseEntity<?> getUpcomingTripAttendeesByMember(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        List<MyTripScheduleRespDto> result =
                tripScheduleService.getUpcomingTripSchedules(principalDetails.getMember().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "사용자의 앞으로의 일정 조회에 성공하였습니다", result), HttpStatus.OK);
    }

    @GetMapping("/my-page/pre-schedules")
    @Operation(summary = "사용자의 지난 일정 조회", description = "사용자의 지난 일정 정보를 반환합니다.")
    public ResponseEntity<?> getPreTripAttendeesByMember(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                         @PageableDefault(size = 5, sort = "tripDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Slice<MyTripScheduleRespDto> result =
                tripScheduleService.getPreTripSchedulesByMemberId(principalDetails.getMember().getId(), pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "사용자의 지난 일정 조회에 성공하였습니다", result), HttpStatus.OK);
    }
}
