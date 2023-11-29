package kr.ac.kumoh.illdang100.tovalley.service.trip_schedule;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripScheduleRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.RescueSupply;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.RescueSupplyRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import static kr.ac.kumoh.illdang100.tovalley.dto.rescue_supply.RescueSupplyRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.trip_schedule.TripScheduleReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.trip_schedule.TripScheduleRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripScheduleServiceImpl implements TripScheduleService {

    private final MemberRepository memberRepository;
    private final TripScheduleRepository tripScheduleRepository;
    private final WaterPlaceRepository waterPlaceRepository;
    private final RescueSupplyRepository rescueSupplyRepository;
    private final ReviewRepository reviewRepository;

    private static final int MAX_TRIP_SCHEDULES = 5;

    /**
     * @param memberId: 사용자 pk
     * @param addTripScheduleReqDto: 새로운 여행 일정 정보
     * @methodnme: addTripSchedule
     * @author: JYeonJun
     * @description: 새로운 여행 일정 등록
     * @return: 해당 달에 대한 사용자들 여행 일정 정보
     */
    @Override
    @Transactional
    public Map<LocalDate, Integer> addTripSchedule(Long memberId, AddTripScheduleReqDto addTripScheduleReqDto) {
        Member findMember = findMemberByIdOrElseThrowEx(memberRepository, memberId);
        Long waterPlaceId = addTripScheduleReqDto.getWaterPlaceId();
        WaterPlace findWaterPlace = findWaterPlaceByIdOrElseThrowEx(waterPlaceRepository, waterPlaceId);
        LocalDate now = LocalDate.now();
        LocalDate tripDate = addTripScheduleReqDto.getTripDate();

        validateTripDate(tripDate, now);

        validateMaxTripSchedules(memberId);

        validateDuplicateTripSchedule(memberId, waterPlaceId, tripDate);

        tripScheduleRepository.save(TripSchedule.builder()
                .member(findMember)
                .waterPlace(findWaterPlace)
                .tripDate(tripDate)
                .tripNumber(addTripScheduleReqDto.getTripPartySize())
                .build());

        return getTripAttendeesByWaterPlace(waterPlaceId, YearMonth.from(tripDate));
    }

    private void validateTripDate(LocalDate tripDate, LocalDate now) {
        if (tripDate.isBefore(now)) {
            throw new CustomApiException("현재 날짜(" + now + ") 이전에는 여행 일정을 추가할 수 없습니다");
        }
    }

    private void validateMaxTripSchedules(Long memberId) {
        int upcomingTripSchedulesCnt = tripScheduleRepository.countByMemberIdAndTripDateGreaterThanEqual(memberId, LocalDate.now());
        if (upcomingTripSchedulesCnt >= MAX_TRIP_SCHEDULES) {
            throw new CustomApiException("여행 일정 추가는 최대 " + MAX_TRIP_SCHEDULES +"개까지 가능합니다");
        }
    }

    private void validateDuplicateTripSchedule(Long memberId, Long waterPlaceId, LocalDate tripDate) {
        if (tripScheduleRepository.existsByMember_IdAndWaterPlace_IdAndTripDate(memberId, waterPlaceId, tripDate)) {
            throw new CustomApiException("이미 존재하는 여행 일정입니다");
        }
    }

    /**
     * @param memberId: 사용자 pk
     * @param tripScheduleIds: 삭제할 여행 일정 pk 리스트
     * @methodnme: deleteTripSchedules
     * @author: JYeonJun
     * @description: 기존 여행 일정 삭제
     * @return:
     */
    @Override
    @Transactional
    public void deleteTripSchedules(Long memberId, List<Long> tripScheduleIds) {
        LocalDate now = LocalDate.now();

        tripScheduleIds.forEach(tripScheduleId -> {

            TripSchedule findTripSchedule = findTripScheduleByIdOrElseThrowEx(tripScheduleRepository, tripScheduleId);

            validatePerMissionForTripScheduleDelete(memberId, tripScheduleId);

            validateTripDateForTripScheduleDelete(findTripSchedule.getTripDate(), now);

            tripScheduleRepository.delete(findTripSchedule);
        });
    }

    private static void validateTripDateForTripScheduleDelete(LocalDate tripDate, LocalDate now) {
        if (tripDate.isBefore(now)) {
            throw new CustomApiException("지난 일정은 삭제할 수 없습니다");
        }
    }

    private void validatePerMissionForTripScheduleDelete(Long memberId, Long tripScheduleId) {
        tripScheduleRepository.findTripScheduleByIdAndMemberId(tripScheduleId, memberId)
                .orElseThrow(() -> new CustomApiException("해당 일정을 삭제할 권한이 없습니다"));
    }

    /**
     * @param waterPlaceId: 물놀이 장소 pk
     * @param yearMonth: 클라이언트로부터 요청받은 날짜 정보
     * @methodnme: getTripAttendeesByWaterPlace
     * @author: JYeonJun
     * @description: 물놀이 장소 여행객 수 조회
     * @return: 요청한 달의 여행객 수
     */
    @Override
    public Map<LocalDate, Integer> getTripAttendeesByWaterPlace(Long waterPlaceId, YearMonth yearMonth) {

        findWaterPlaceByIdOrElseThrowEx(waterPlaceRepository, waterPlaceId);

        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        List<TripSchedule> findTripSchedules =
                tripScheduleRepository.findByWaterPlaceIdAndTripDateBetween(waterPlaceId, firstDayOfMonth, lastDayOfMonth);

        Map<LocalDate, Integer> attendeesByDate = new HashMap<>();
        for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
            LocalDate tmpDate = date;
            int attendees = findTripSchedules.stream()
                    .filter(tripSchedule -> tripSchedule.getTripDate().equals(tmpDate))
                    .mapToInt(TripSchedule::getTripNumber)
                    .sum();
            attendeesByDate.put(date, attendees);
        }

        return attendeesByDate;
    }

    /**
     * @param memberId: 사용자 pk
     * @methodnme: getUpcomingTripSchedules
     * @author: JYeonJun
     * @description: 앞으로의 여행 일정 조회
     * @return: 앞으로의 여행 일정 리스트
     */
    @Override
    public List<MyTripScheduleRespDto> getUpcomingTripSchedules(Long memberId) {
        LocalDate today = LocalDate.now();
        List<TripSchedule> tripSchedules = tripScheduleRepository.findUpcomingWithWaterPlaceByMemberOrderByTripDateASC(memberId, today);

        Map<Long, Map<LocalDate, Integer>> waterPlaceTrafficMap = calculateWaterPlaceTraffic(tripSchedules);
        Map<Long, RescueSupplyByWaterPlaceRespDto> rescueSupplyMap = getRescueSupplies(tripSchedules);

        return tripSchedules.stream().map(t -> {
            WaterPlace waterPlace = t.getWaterPlace();
            Long waterPlaceId = waterPlace.getId();
            LocalDate tripDate = t.getTripDate();

            int waterPlaceTraffic = waterPlaceTrafficMap
                    .getOrDefault(waterPlaceId, Collections.emptyMap())
                    .getOrDefault(tripDate, 0);

            RescueSupplyByWaterPlaceRespDto rescueSupplyRespDto = rescueSupplyMap.get(waterPlaceId);

            return new MyTripScheduleRespDto(
                    t.getId(),
                    waterPlaceId,
                    waterPlace.getWaterPlaceName(),
                    waterPlace.getWaterPlaceImage(),
                    waterPlace.getAddress(),
                    waterPlace.getRating(),
                    waterPlace.getReviewCount(),
                    waterPlaceTraffic,
                    tripDate,
                    rescueSupplyRespDto,
                    t.getTripNumber(),
                    null
            );
        }).collect(Collectors.toList());
    }

    /**
     * @param memberId: 사용자 pk
     * @param pageable: 페이징 조건
     * @methodnme: getPreTripSchedulesByMemberId
     * @author: JYeonJun
     * @description: 지난 여행 일정 조회
     * @return: 지난 여행 일정 Slice
     */
    @Override
    public Slice<MyTripScheduleRespDto> getPreTripSchedulesByMemberId(Long memberId, Pageable pageable) {

        Slice<MyTripScheduleRespDto> sliceMyTripSchedules =
                tripScheduleRepository.findSliceMyTripSchedulesByMemberId(memberId, pageable, LocalDate.now());

        List<MyTripScheduleRespDto> myTripSchedules = sliceMyTripSchedules.getContent();

        Map<Long, Map<LocalDate, Integer>> waterPlaceTrafficMap = calculateWaterPlaceTrafficForMyTrip(myTripSchedules);
        Map<Long, RescueSupplyByWaterPlaceRespDto> rescueSupplyMap = getRescueSuppliesForMyTrip(myTripSchedules);

        sliceMyTripSchedules.stream().forEach(mt -> {

            Long waterPlaceId = mt.getWaterPlaceId();
            LocalDate tripDate = mt.getTripDate();

            int waterPlaceTraffic = waterPlaceTrafficMap
                    .getOrDefault(waterPlaceId, Collections.emptyMap())
                    .getOrDefault(tripDate, 0);

            RescueSupplyByWaterPlaceRespDto rescueSupplyRespDto = rescueSupplyMap.get(waterPlaceId);
            boolean hasReview = reviewRepository.existsByTripScheduleId(mt.getTripScheduleId());

            mt.changeWaterPlaceTraffic(waterPlaceTraffic);
            mt.changeRescueSupplies(rescueSupplyRespDto);
            mt.changeHasReview(hasReview);
        });

        return sliceMyTripSchedules;
    }

    private Map<Long, Map<LocalDate, Integer>> calculateWaterPlaceTraffic(List<TripSchedule> tripSchedules) {
        Map<Long, Map<LocalDate, Integer>> waterPlaceTrafficMap = new HashMap<>();
        for (TripSchedule tripSchedule : tripSchedules) {
            Long waterPlaceId = tripSchedule.getWaterPlace().getId();
            LocalDate tripDate = tripSchedule.getTripDate();

            Map<LocalDate, Integer> dateTrafficMap = waterPlaceTrafficMap
                    .computeIfAbsent(waterPlaceId, k -> new HashMap<>());

            dateTrafficMap.put(tripDate, dateTrafficMap.getOrDefault(tripDate, 0) + tripSchedule.getTripNumber());
        }
        return waterPlaceTrafficMap;
    }

    private Map<Long, Map<LocalDate, Integer>> calculateWaterPlaceTrafficForMyTrip(List<MyTripScheduleRespDto> myTripSchedules) {
        Map<Long, Map<LocalDate, Integer>> waterPlaceTrafficMap = new HashMap<>();
        for (MyTripScheduleRespDto myTripSchedule : myTripSchedules) {
            Long waterPlaceId = myTripSchedule.getWaterPlaceId();
            LocalDate tripDate = myTripSchedule.getTripDate();

            Map<LocalDate, Integer> dateTrafficMap = waterPlaceTrafficMap
                    .computeIfAbsent(waterPlaceId, k -> new HashMap<>());

            dateTrafficMap.put(tripDate, dateTrafficMap.getOrDefault(tripDate, 0) + myTripSchedule.getTripPartySize());
        }
        return waterPlaceTrafficMap;
    }

    private Map<Long, RescueSupplyByWaterPlaceRespDto> getRescueSupplies(List<TripSchedule> tripSchedules) {
        Set<Long> waterPlaceIds = tripSchedules.stream().map(t -> t.getWaterPlace().getId()).collect(Collectors.toSet());
        List<RescueSupply> rescueSupplies = rescueSupplyRepository.findByWaterPlace_IdIn(waterPlaceIds);

        return rescueSupplies.stream().collect(Collectors.toMap(
                supply -> supply.getWaterPlace().getId(),
                RescueSupplyByWaterPlaceRespDto::createRescueSupplyRespDto
        ));
    }

    private Map<Long, RescueSupplyByWaterPlaceRespDto> getRescueSuppliesForMyTrip(List<MyTripScheduleRespDto> myTripSchedules) {
        Set<Long> waterPlaceIds = myTripSchedules.stream().map(MyTripScheduleRespDto::getWaterPlaceId).collect(Collectors.toSet());
        List<RescueSupply> rescueSupplies = rescueSupplyRepository.findByWaterPlace_IdIn(waterPlaceIds);

        return rescueSupplies.stream().collect(Collectors.toMap(
                supply -> supply.getWaterPlace().getId(),
                RescueSupplyByWaterPlaceRespDto::createRescueSupplyRespDto
        ));
    }
}
