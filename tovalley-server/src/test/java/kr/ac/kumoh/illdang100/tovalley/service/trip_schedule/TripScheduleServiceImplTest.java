package kr.ac.kumoh.illdang100.tovalley.service.trip_schedule;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripScheduleRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kr.ac.kumoh.illdang100.tovalley.dto.trip_schedule.TripScheduleReqDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripScheduleServiceImplTest extends DummyObject {

    @InjectMocks
    private TripScheduleServiceImpl tripScheduleService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TripScheduleRepository tripScheduleRepository;
    @Mock
    private WaterPlaceRepository waterPlaceRepository;

    @Test
    public void addTripSchedule_validate_date_test() {

        // given
        Long memberId = 1L;
        Member member = newMockMember(memberId, "test_1234", "testNick", MemberEnum.CUSTOMER);
        Long waterPlaceId = 1L;
        WaterPlace waterPlace = newMockWaterPlace(waterPlaceId, "금오계곡", "경상북도", 1.1, 5);
        LocalDate now = LocalDate.now();
        LocalDate tripDate = now.minusDays(1);
        Integer tripPartySize = 10;

        TripSchedule tripSchedule = newMockTripSchedule(1L, member, waterPlace, tripDate, tripPartySize);

        AddTripScheduleReqDto addTripScheduleReqDto =
                new AddTripScheduleReqDto(waterPlaceId, tripDate, tripPartySize);

        // stub1
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        // stub2
        when(waterPlaceRepository.findById(any())).thenReturn(Optional.of(waterPlace));

        // when

        // then
        assertThatThrownBy(() -> tripScheduleService.addTripSchedule(memberId, addTripScheduleReqDto))
                .isInstanceOf(CustomApiException.class)
                .hasMessageContaining("현재 날짜(" + now + ") 이전에는 여행 일정을 추가할 수 없습니다");
    }

    @Test
    public void addTripSchedule_validate_max_schedules_test() {

        // given
        Long memberId = 1L;
        Member member = newMockMember(memberId, "test_1234", "testNick", MemberEnum.CUSTOMER);
        Long waterPlaceId = 1L;
        WaterPlace waterPlace = newMockWaterPlace(waterPlaceId, "금오계곡", "경상북도", 1.1, 5);
        LocalDate tripDate = LocalDate.now().plusDays(10);
        Integer tripPartySize = 10;

        TripSchedule tripSchedule = newMockTripSchedule(1L, member, waterPlace, tripDate, tripPartySize);

        AddTripScheduleReqDto addTripScheduleReqDto =
                new AddTripScheduleReqDto(waterPlaceId, tripDate, tripPartySize);

        // stub1
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        // stub2
        when(waterPlaceRepository.findById(any())).thenReturn(Optional.of(waterPlace));
        // stub3
        when(tripScheduleRepository.countByMemberIdAndTripDateGreaterThanEqual(any(), any())).thenReturn(5);

        // when

        // then
        assertThatThrownBy(() -> tripScheduleService.addTripSchedule(memberId, addTripScheduleReqDto))
                .isInstanceOf(CustomApiException.class)
                .hasMessageContaining("여행 일정 추가는 최대 5개까지 가능합니다");
    }

    @Test
    public void addTripSchedule_validate_duplicate_schedules_test() {

        // given
        Long memberId = 1L;
        Member member = newMockMember(memberId, "test_1234", "testNick", MemberEnum.CUSTOMER);
        Long waterPlaceId = 1L;
        WaterPlace waterPlace = newMockWaterPlace(waterPlaceId, "금오계곡", "경상북도", 1.1, 5);
        LocalDate tripDate = LocalDate.now().plusDays(10);
        Integer tripPartySize = 10;

        TripSchedule tripSchedule = newMockTripSchedule(1L, member, waterPlace, tripDate, tripPartySize);

        AddTripScheduleReqDto addTripScheduleReqDto =
                new AddTripScheduleReqDto(waterPlaceId, tripDate, tripPartySize);

        // stub1
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        // stub2
        when(waterPlaceRepository.findById(any())).thenReturn(Optional.of(waterPlace));
        // stub3
        when(tripScheduleRepository.countByMemberIdAndTripDateGreaterThanEqual(any(), any())).thenReturn(0);
        // stub4
        when(tripScheduleRepository.existsByMember_IdAndWaterPlace_IdAndTripDate(any(), any(), any())).thenReturn(true);

        // when

        // then
        assertThatThrownBy(() -> tripScheduleService.addTripSchedule(memberId, addTripScheduleReqDto))
                .isInstanceOf(CustomApiException.class)
                .hasMessageContaining("이미 존재하는 여행 일정입니다");
    }

    @Test
    public void deleteTripSchedules_validate_permission_test() {

        // given
        Long memberId = 1L;
        Long testMemberId = 2L;
        Member member = newMockMember(memberId, "test_1234", "testNick", MemberEnum.CUSTOMER);
        Long waterPlaceId = 1L;
        WaterPlace waterPlace = newMockWaterPlace(waterPlaceId, "금오계곡", "경상북도", 1.1, 5);
        LocalDate tripDate = LocalDate.now().plusDays(10);
        Integer tripPartySize = 10;

        List<Long> tripScheduleList = Arrays.asList(1L, 2L, 3L);

        TripSchedule tripSchedule1 = newMockTripSchedule(tripScheduleList.get(0), member, waterPlace, tripDate, tripPartySize);
        TripSchedule tripSchedule2 = newMockTripSchedule(tripScheduleList.get(1), member, waterPlace, tripDate, tripPartySize);
        TripSchedule tripSchedule3 = newMockTripSchedule(tripScheduleList.get(2), member, waterPlace, tripDate, tripPartySize);

        // stub1
        when(tripScheduleRepository.findById(any())).thenReturn(Optional.of(tripSchedule1));

        // when

        // then
        assertThatThrownBy(() -> tripScheduleService.deleteTripSchedules(testMemberId, tripScheduleList))
                .isInstanceOf(CustomApiException.class)
                .hasMessageContaining("해당 일정을 삭제할 권한이 없습니다");
    }

    @Test
    public void deleteTripSchedules_validate_tripDate_test() {

        // given
        Long memberId = 1L;
        Member member = newMockMember(memberId, "test_1234", "testNick", MemberEnum.CUSTOMER);
        Long waterPlaceId = 1L;
        WaterPlace waterPlace = newMockWaterPlace(waterPlaceId, "금오계곡", "경상북도", 1.1, 5);
        LocalDate tripDate = LocalDate.now().minusDays(10);
        Integer tripPartySize = 10;

        List<Long> tripScheduleList = Arrays.asList(1L, 2L, 3L);

        TripSchedule tripSchedule1 = newMockTripSchedule(tripScheduleList.get(0), member, waterPlace, tripDate, tripPartySize);
        TripSchedule tripSchedule2 = newMockTripSchedule(tripScheduleList.get(1), member, waterPlace, tripDate, tripPartySize);
        TripSchedule tripSchedule3 = newMockTripSchedule(tripScheduleList.get(2), member, waterPlace, tripDate, tripPartySize);

        // stub1
        when(tripScheduleRepository.findById(any())).thenReturn(Optional.of(tripSchedule1));

        // stub2
        when(tripScheduleRepository.findTripScheduleByIdAndMemberId(any(), any())).thenReturn(Optional.of(tripSchedule1));

        // when

        // then
        assertThatThrownBy(() -> tripScheduleService.deleteTripSchedules(memberId, tripScheduleList))
                .isInstanceOf(CustomApiException.class)
                .hasMessageContaining("지난 일정은 삭제할 수 없습니다");
    }
}