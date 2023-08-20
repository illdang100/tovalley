package kr.ac.kumoh.illdang100.tovalley.service.page;

import kr.ac.kumoh.illdang100.tovalley.domain.ProvinceEnum;
import kr.ac.kumoh.illdang100.tovalley.service.accident.AccidentService;
import kr.ac.kumoh.illdang100.tovalley.service.review.ReviewService;
import kr.ac.kumoh.illdang100.tovalley.service.trip_schedule.TripScheduleService;
import kr.ac.kumoh.illdang100.tovalley.service.water_place.WaterPlaceService;
import kr.ac.kumoh.illdang100.tovalley.service.weather.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.PageRespDto.*;

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
     * @methodnme: getWaterPlaceDetailPageAllData
     * @author: JYeonJun
     * @description: 물놀이 장소 상세보기 페이지 정보 조회
     * @return: 물놀이 장소 상세정보, 날씨, 사건사고 정보, 여행객 수, 리뷰
     */
    @Override
    @Transactional
    public WaterPlaceDetailAllRespDto getWaterPlaceDetailPageAllData(Long waterPlaceId, Pageable pageable) {
        List<DailyWaterPlaceWeatherDto> waterPlaceWeathers = getWaterPlaceWeathers(waterPlaceId);
        WaterPlaceDetailRespDto waterPlaceDetail = getWaterPlaceDetail(waterPlaceId);
        RescueSupplyRespDto rescueSupplies = getRescueSupplies(waterPlaceId);
        WaterPlaceAccidentFor5YearsDto accidentsFor5Years = getAccidentsFor5Years(waterPlaceId);
        Map<LocalDate, Integer> travelPlans = getTravelPlans(waterPlaceId);
        WaterPlaceReviewDetailRespDto reviewDetailRespDto = getReviews(waterPlaceId, pageable);

        return new WaterPlaceDetailAllRespDto(waterPlaceWeathers, waterPlaceDetail, rescueSupplies,
                accidentsFor5Years, travelPlans, reviewDetailRespDto);
    }

    private List<DailyWaterPlaceWeatherDto> getWaterPlaceWeathers(Long waterPlaceId) {
        return weatherService.getWaterPlaceWeatherData(waterPlaceId);
    }

    private WaterPlaceDetailRespDto getWaterPlaceDetail(Long waterPlaceId) {
        return waterPlaceService.getWaterPlaceDetailByWaterPlace(waterPlaceId);
    }

    private RescueSupplyRespDto getRescueSupplies(Long waterPlaceId) {
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

    @Override
    public MyPageAllRespDto getMyPageAllData() {
        return null;
    }
}
