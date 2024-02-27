package kr.ac.kumoh.illdang100.tovalley.service.page;

import kr.ac.kumoh.illdang100.tovalley.domain.ProvinceEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.comment.CommentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoard;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardImageRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.service.accident.AccidentService;
import kr.ac.kumoh.illdang100.tovalley.service.member.MemberService;
import kr.ac.kumoh.illdang100.tovalley.service.review.ReviewService;
import kr.ac.kumoh.illdang100.tovalley.service.trip_schedule.TripScheduleService;
import kr.ac.kumoh.illdang100.tovalley.service.water_place.WaterPlaceService;
import kr.ac.kumoh.illdang100.tovalley.service.weather.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardRespDto.LostFoundBoardDetailRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.member.MemberRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.page.PageRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.rescue_supply.RescueSupplyRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.review.ReviewRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.trip_schedule.TripScheduleRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.weather.WeatherRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.findLostFoundBoardByIdWithMemberOrElseThrowEx;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PageServiceImpl implements PageService{
    private final WeatherService weatherService;
    private final AccidentService accidentService;
    private final WaterPlaceService waterPlaceService;
    private final ReviewService reviewService;
    private final TripScheduleService tripScheduleService;
    private final MemberService memberService;
    private final LostFoundBoardRepository lostFoundBoardRepository;
    private final CommentRepository commentRepository;
    private final LostFoundBoardImageRepository lostFoundBoardImageRepository;

    /**
     * @methodnme: getMainPageAllData
     * @author: JYeonJun
     * @description: 메인페이지 정보 요청시 데이터를 취합해서 보내주는 메서드
     *
     * @return: 전국 날씨 정보, 특보, 예비 특보, 지역별 사건 사고 발생률, 인기 계곡 현황
     */
    @Override
    public MainPageAllRespDto getMainPageAllData() {

        List<NationalWeatherRespDto> nationalWeatherDto = weatherService.getNationalWeathers();
        AlertRespDto alertRespDto = weatherService.getAllSpecialWeathers();
        AccidentCountDto nationalAccidentCountDto = accidentService.getAccidentCntPerMonthByProvince(ProvinceEnum.NATIONWIDE.getValue());
        List<NationalPopularWaterPlacesDto> popularWaterPlaces = waterPlaceService.getPopularWaterPlaces("RATING");

        return new MainPageAllRespDto(nationalWeatherDto, alertRespDto, nationalAccidentCountDto, popularWaterPlaces);
    }

    /**
     * @param waterPlaceId: 물놀이 장소 pk
     * @param pageable: 페이징 정보
     * @methodnme: getWaterPlaceDetailPageAllData
     * @author: JYeonJun
     * @description: 물놀이 장소 상세보기 페이지 정보 조회
     * @return: 물놀이 장소 상세정보, 날씨, 사건사고 정보, 여행객 수, 리뷰
     */
    @Override
    @Transactional
    public WaterPlaceDetailPageAllRespDto getWaterPlaceDetailPageAllData(Long waterPlaceId, Pageable pageable) {
        List<DailyWaterPlaceWeatherDto> waterPlaceWeathers = getWaterPlaceWeathers(waterPlaceId);
        WaterPlaceDetailRespDto waterPlaceDetail = getWaterPlaceDetail(waterPlaceId);
        RescueSupplyByWaterPlaceRespDto rescueSupplies = getRescueSupplies(waterPlaceId);
        WaterPlaceAccidentFor5YearsDto accidentsFor5Years = getAccidentsFor5Years(waterPlaceId);
        Map<LocalDate, Integer> travelPlans = getTravelPlans(waterPlaceId);
        WaterPlaceReviewDetailRespDto reviewDetailRespDto = getReviews(waterPlaceId, pageable);

        return new WaterPlaceDetailPageAllRespDto(waterPlaceWeathers, waterPlaceDetail, rescueSupplies,
                accidentsFor5Years, travelPlans, reviewDetailRespDto);
    }

    private List<DailyWaterPlaceWeatherDto> getWaterPlaceWeathers(Long waterPlaceId) {
        return weatherService.getWaterPlaceWeatherData(waterPlaceId);
    }

    private WaterPlaceDetailRespDto getWaterPlaceDetail(Long waterPlaceId) {
        return waterPlaceService.getWaterPlaceDetailByWaterPlace(waterPlaceId);
    }

    private RescueSupplyByWaterPlaceRespDto getRescueSupplies(Long waterPlaceId) {
        return waterPlaceService.getRescueSuppliesByWaterPlace(waterPlaceId);
    }

    private WaterPlaceAccidentFor5YearsDto getAccidentsFor5Years(Long waterPlaceId) {
        return accidentService.getAccidentsFor5YearsByWaterPlace(waterPlaceId);
    }

    private Map<LocalDate, Integer> getTravelPlans(Long waterPlaceId) {
        return tripScheduleService.getTripAttendeesByWaterPlace(waterPlaceId, YearMonth.now());
    }

    private WaterPlaceReviewDetailRespDto getReviews(Long waterPlaceId, Pageable pageable) {
        return reviewService.getReviewsByWaterPlaceId(waterPlaceId, pageable);
    }

    /**
     * @param memberId: 사용자 pk
     * @param pageable: 페이징 정보
     * @methodnme: getMyPageAllData
     * @author: JYeonJun
     * @description: 마이 페이지 정보 조회
     * @return: 개인정보, 리뷰, 앞으로의 일정
     */
    @Override
    public MyPageAllRespDto getMyPageAllData(Long memberId, Pageable pageable) {

        // 개인정보
        MemberProfileRespDto memberDetail = memberService.getMemberDetail(memberId);

        // 내리뷰
        Slice<MyReviewRespDto> reviewsByMemberId = reviewService.getReviewsByMemberId(memberId, pageable);

        // 앞으로의 일정
        List<MyTripScheduleRespDto> upcomingTripSchedules = tripScheduleService.getUpcomingTripSchedules(memberId);

        return new MyPageAllRespDto(memberDetail, reviewsByMemberId, upcomingTripSchedules);
    }

    /**
     * 분실물 찾기 게시글 조회
     * @param LostFoundBoardListReqDto
     * @param pageable
     * @return
     */
    @Override
    public Slice<LostFoundBoardListRespDto> getLostFoundBoardList(LostFoundBoardListReqDto LostFoundBoardListReqDto, Pageable pageable) {
        return lostFoundBoardRepository.getLostFoundBoardListBySlice(LostFoundBoardListReqDto, pageable);
    }

    /**
     * 분실물 찾기 게시글 상세 페이지 조회
     * @param lostFoundBoardId
     * @param member
     * @return
     */
    @Override
    public LostFoundBoardDetailRespDto getLostFoundBoardDetail(long lostFoundBoardId, Member member) {

        LostFoundBoard foundLostFoundBoard = findLostFoundBoardByIdWithMemberOrElseThrowEx(lostFoundBoardRepository, lostFoundBoardId);

        boolean isMyBoard = member != null && isMyBoard(foundLostFoundBoard, member.getId());
        List<CommentDetailRespDto> commentDetails = findCommentDetails(lostFoundBoardId, member != null ? member.getEmail() : null);
        List<String> imageUrls = lostFoundBoardImageRepository.findImageByLostFoundBoardId(lostFoundBoardId);
        long commentCount = commentRepository.countByLostFoundBoardId(lostFoundBoardId);

        return createLostFoundBoardDetailRespDto(foundLostFoundBoard, isMyBoard, commentDetails, imageUrls, commentCount);
    }

    private Boolean isMyBoard(LostFoundBoard lostFoundBoard, Long memberId) {
        return lostFoundBoard.getMember().getId().equals(memberId);
    }

    private List<CommentDetailRespDto> findCommentDetails(long lostFoundBoardId, String memberEmail) {
        return commentRepository.findCommentByLostFoundBoardId(lostFoundBoardId)
                .stream()
                .map(c -> new CommentDetailRespDto(c.getId(), c.getAuthorEmail(), c.getContent(), c.getCreatedDate(), memberEmail != null && isMyComment(memberEmail, c.getAuthorEmail())))
                .collect(Collectors.toList());
    }

    private boolean isMyComment(String memberEmail, String authorEmail) {
        return (memberEmail.equals(authorEmail));
    }
}
