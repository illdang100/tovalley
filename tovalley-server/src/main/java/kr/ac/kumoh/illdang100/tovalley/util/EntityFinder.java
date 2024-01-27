package kr.ac.kumoh.illdang100.tovalley.util;

import kr.ac.kumoh.illdang100.tovalley.domain.accident.Accident;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.email_code.EmailCode;
import kr.ac.kumoh.illdang100.tovalley.domain.email_code.EmailCodeRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoard;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripScheduleRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.*;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshToken;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshTokenRedisRepository;

public class EntityFinder {

    public static Member findMemberByIdOrElseThrowEx(MemberRepository memberRepository, Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomApiException("사용자[" + memberId + "]가 존재하지 않습니다"));
    }

    public static WaterPlace findWaterPlaceByIdOrElseThrowEx(WaterPlaceRepository waterPlaceRepository, Long waterPlaceId) {
        return waterPlaceRepository.findById(waterPlaceId)
                .orElseThrow(() -> new CustomApiException("물놀이 장소[" + waterPlaceId + "]가 존재하지 않습니다"));
    }

    public static WaterPlaceDetail findWaterPlaceDetailWithWaterPlaceByWaterPlaceIdOrElseThrowEx(WaterPlaceDetailRepository waterPlaceDetailRepository, Long waterPlaceId) {
        return waterPlaceDetailRepository.findWaterPlaceDetailWithWaterPlaceByWaterPlaceId(waterPlaceId)
                .orElseThrow(() -> new CustomApiException("물놀이 장소[" + waterPlaceId + "]가 존재하지 않습니다"));
    }

    public static WaterPlaceDetail findWaterPlaceDetailByWaterPlaceIdOrElseThrowEx(WaterPlaceDetailRepository waterPlaceDetailRepository, Long waterPlaceId) {
        return waterPlaceDetailRepository.findByWaterPlaceId(waterPlaceId)
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

    public static Member findMemberByEmailOrElseThrowEx(MemberRepository memberRepository, String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomApiException("사용자[" + email + "]가 존재하지 않습니다"));
    }

    public static RefreshToken findRefreshTokenOrElseThrowEx(RefreshTokenRedisRepository refreshTokenRedisRepository,
                                                             String refreshTokenId) {
        return refreshTokenRedisRepository.findById(refreshTokenId)
                .orElseThrow(() -> new CustomApiException("토큰 갱신에 실패했습니다"));
    }

    public static EmailCode findEmailCodeByEmailOrElseThrowEx(EmailCodeRepository emailCodeRepository, String email) {
        return emailCodeRepository.findByEmail(email)
                .orElseThrow(() -> new CustomApiException("사용자[" + email + "]에게 발급된 인증 코드가 존재하지 않습니다"));
    }

    public static Accident findAccidentByIdAndWaterPlaceIdOrElseThrowEx(AccidentRepository accidentRepository,
                                                                        Long accidentId, Long waterPlaceId) {

        return accidentRepository.findByIdAndWaterPlaceId(accidentId, waterPlaceId)
                .orElseThrow(() -> new CustomApiException("물놀이 장소[" + waterPlaceId + "]에 대한 사고[" + accidentId + "]가 존재하지 않습니다"));
    }

    public static LostFoundBoard findLostFoundBoardByIdOrElseThrow(LostFoundBoardRepository lostFoundBoardRepository, long lostFoundBoardId) {
        return lostFoundBoardRepository.findById(lostFoundBoardId).orElseThrow(() -> new CustomApiException("분실물 찾기 게시글[" + lostFoundBoardId + "]이 존재하지 않습니다"));
    }
}
