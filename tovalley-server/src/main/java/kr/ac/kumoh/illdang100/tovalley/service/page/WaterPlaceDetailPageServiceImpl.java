package kr.ac.kumoh.illdang100.tovalley.service.page;

import kr.ac.kumoh.illdang100.tovalley.service.domain.accident.AccidentService;
import kr.ac.kumoh.illdang100.tovalley.service.domain.review.ReviewService;
import kr.ac.kumoh.illdang100.tovalley.service.domain.trip_schedule.TripScheduleService;
import kr.ac.kumoh.illdang100.tovalley.service.domain.water_place.WaterPlaceService;
import kr.ac.kumoh.illdang100.tovalley.service.domain.weather.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.WaterPlaceDetailPageRespDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaterPlaceDetailPageServiceImpl implements WaterPlaceDetailPageService {

    private final ReviewService reviewService;
    private final WeatherService weatherService;
    private final WaterPlaceService waterPlaceService;
    private final AccidentService accidentService;
    private final TripScheduleService tripScheduleService;

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
}
