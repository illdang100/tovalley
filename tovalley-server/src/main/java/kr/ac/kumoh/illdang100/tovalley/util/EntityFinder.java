package kr.ac.kumoh.illdang100.tovalley.util;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripScheduleRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.*;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;

public class EntityFinder {

    public static Member findMemberByIdOrElseThrowEx(MemberRepository memberRepository, Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomApiException("사용자[" + memberId + "]가 존재하지 않습니다"));
    }

    public static WaterPlace findWaterPlaceByIdOrElseThrowEx(WaterPlaceRepository waterPlaceRepository, Long waterPlaceId) {
        return waterPlaceRepository.findById(waterPlaceId)
                .orElseThrow(() -> new CustomApiException("물놀이 장소[" + waterPlaceId + "]가 존재하지 않습니다"));
    }

    public static WaterPlaceDetail findWaterPlaceDetailByWaterPlaceIdOrElseThrowEx(WaterPlaceDetailRepository waterPlaceDetailRepository, Long waterPlaceId) {
        return waterPlaceDetailRepository.findWaterPlaceDetailWithWaterPlaceById(waterPlaceId)
                .orElseThrow(() -> new CustomApiException("물놀이 장소[" + waterPlaceId + "]가 존재하지 않습니다"));
    }

    public static RescueSupply findRescueSupplyByWaterPlaceIdOrElseThrowEx(RescueSupplyRepository rescueSupplyRepository, Long waterPlaceId) {
        return rescueSupplyRepository.findByWaterPlace_Id(waterPlaceId)
                .orElseThrow(() -> new CustomApiException("물놀이 장소[" + waterPlaceId + "]: 구급용품이 존재하지 않습니다"));
    }

    public static TripSchedule findTripScheduleByIdOrElseThrowEx(TripScheduleRepository tripScheduleRepository, Long tripScheduleId) {
        return tripScheduleRepository.findById(tripScheduleId)
                .orElseThrow(() -> new CustomApiException("여행 일정[" + tripScheduleId + "]이 존재하지 않습니다"));
    }
}
