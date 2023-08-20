package kr.ac.kumoh.illdang100.tovalley.web.page;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.Accident;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.*;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripScheduleRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.*;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.water_place_weather.WaterPlaceWeather;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.water_place_weather.WaterPlaceWeatherRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Sql("classpath:db/teardown.sql")
class WaterPlaceDetailPageApiControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private WaterPlaceWeatherRepository waterPlaceWeatherRepository;
    @Autowired
    private WaterPlaceRepository waterPlaceRepository;
    @Autowired
    private AccidentRepository accidentRepository;
    @Autowired
    private WaterPlaceDetailRepository waterPlaceDetailRepository;
    @Autowired
    private RescueSupplyRepository rescueSupplyRepository;
    @Autowired
    private TripScheduleRepository tripScheduleRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewImageRepository reviewImageRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        dataSetting();
    }

    @Test
    public void getWaterPlaceDetailPage_test() throws Exception {

        // given


        // when
        ResultActions resultActions =
                mvc.perform(get("/api/auth/water-places/1?sort=createdDate,desc&page=0&size=5"));

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        LocalDate firstDay = LocalDate.of(year, month, 1);
        String string = firstDay.toString();

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.waterPlaceWeathers.size()").value(5))
                .andExpect(jsonPath("$.data.waterPlaceDetails.waterPlaceName").value("서울계곡"))
                .andExpect(jsonPath("$.data.waterPlaceDetails.waterQualityReviews['깨끗해요']").value(3))
                .andExpect(jsonPath("$.data.rescueSupplies.lifeBoatNum").value(10))
                .andExpect(jsonPath("$.data.accidents.totalDeathCnt").value(12))
                .andExpect(jsonPath("$.data.tripPlanToWaterPlace").isMap())
                .andExpect(jsonPath("$.data.reviewRespDto.waterPlaceRating").value(3.2))
                .andExpect(jsonPath("$.data.reviewRespDto.reviewCnt").value(6))
                .andExpect(jsonPath("$.data.reviewRespDto.ratingRatio['1']").value(1))
                .andExpect(jsonPath("$.data.reviewRespDto.reviews.content[0].reviewId").value(6))
                .andExpect(jsonPath("$.data.reviewRespDto.reviews.content[4].reviewId").value(2))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getWaterPlaceDetailPage_빈리스트_test() throws Exception {

        // given

        // when
        ResultActions resultActions =
                mvc.perform(get("/api/auth/water-places/2?sort=rating,desc&page=0&size=5"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.waterPlaceWeathers.size()").value(5))
                .andExpect(jsonPath("$.data.waterPlaceDetails.waterPlaceName").value("금오계곡"))
                .andExpect(jsonPath("$.data.rescueSupplies.lifeBoatNum").value(10))
                .andExpect(jsonPath("$.data.accidents.totalDeathCnt").value(0))
                .andExpect(jsonPath("$.data.reviewRespDto.waterPlaceRating").value(3.2))
                .andExpect(jsonPath("$.data.reviewRespDto.reviewCnt").value(6))
                .andExpect(jsonPath("$.data.reviewRespDto.ratingRatio['1']").value(1))
                .andExpect(jsonPath("$.data.reviewRespDto.reviews.content[0].reviewId").value(12))
                .andExpect(jsonPath("$.data.reviewRespDto.reviews.content[4].reviewId").value(7))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getWaterPlaceDetailPage_날짜_갱신_test() throws Exception {

        // given

        // when
        ResultActions resultActions =
                mvc.perform(get("/api/auth/water-places/3?sort=createdDate,desc&page=0&size=5"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.waterPlaceWeathers.size()").value(5))
                .andExpect(jsonPath("$.data.waterPlaceWeathers[0].weatherDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.data.waterPlaceDetails.waterPlaceName").value("울릉계곡"))
                .andExpect(jsonPath("$.data.rescueSupplies.lifeBoatNum").value(10))
                .andExpect(jsonPath("$.data.accidents.totalDeathCnt").value(0))
                .andExpect(jsonPath("$.data.reviewRespDto.waterPlaceRating").value(0.0))
                .andExpect(jsonPath("$.data.reviewRespDto.reviewCnt").value(0))
                .andExpect(jsonPath("$.data.reviewRespDto.ratingRatio['1']").value(0))
                .andDo(MockMvcResultHandlers.print());
    }

    private void dataSetting() {

        List<Member> memberList = new ArrayList<>();
        Member member1 = newMember("kakao_1234", "member1");
        Member member2 = newMember("kakao_5678", "member2");
        Member member3 = newMember("kakao_9101", "member3");
        Member member4 = newMember("kakao_1121", "member4");
        Member member5 = newMember("kakao_3141", "member5");
        Member member6 = newMember("kakao_4231", "member6");
        memberList.add(member1);
        memberList.add(member2);
        memberList.add(member3);
        memberList.add(member4);
        memberList.add(member5);
        memberList.add(member6);
        memberRepository.saveAll(memberList);

        List<WaterPlace> waterPlaceList = new ArrayList<>();
        WaterPlace waterPlace = newWaterPlace("서울계곡", "서울특별시", 0.0, 0);
        WaterPlace waterPlace2 = newWaterPlace("금오계곡", "경상북도", 0.0, 0);
        WaterPlace waterPlace3 = newWaterPlace("울릉계곡", "경상북도", 0.0, 0);
        waterPlaceList.add(waterPlace);
        waterPlaceList.add(waterPlace2);
        waterPlaceList.add(waterPlace3);
        waterPlaceRepository.saveAll(waterPlaceList);

        List<WaterPlaceDetail> waterPlaceDetailList = new ArrayList<>();
        waterPlaceDetailList.add(newWaterPlaceDetail(waterPlace));
        waterPlaceDetailList.add(newWaterPlaceDetail(waterPlace2));
        waterPlaceDetailList.add(newWaterPlaceDetail(waterPlace3));
        waterPlaceDetailRepository.saveAll(waterPlaceDetailList);

        List<RescueSupply> rescueSupplyList = new ArrayList<>();
        rescueSupplyList.add(newRescueSupply(waterPlace));
        rescueSupplyList.add(newRescueSupply(waterPlace2));
        rescueSupplyList.add(newRescueSupply(waterPlace3));
        rescueSupplyRepository.saveAll(rescueSupplyList);

        List<Accident> accidentList = new ArrayList<>();
        accidentList.add(newAccident(waterPlace, LocalDate.now(), AccidentEnum.INJURY, 16));
        accidentList.add(newAccident(waterPlace, LocalDate.now(), AccidentEnum.DEATH, 6));
        accidentList.add(newAccident(waterPlace, LocalDate.now(), AccidentEnum.DISAPPEARANCE, 6));
        accidentList.add(newAccident(waterPlace, LocalDate.now().minusYears(4), AccidentEnum.DEATH, 6));
        accidentList.add(newAccident(waterPlace, LocalDate.now().minusYears(6), AccidentEnum.DEATH, 6));
        accidentList.add(newAccident(waterPlace, LocalDate.now().minusYears(10), AccidentEnum.DEATH, 6));
        accidentRepository.saveAll(accidentList);

        List<WaterPlaceWeather> waterPlaceWeatherList = new ArrayList<>();
        LocalDate now = LocalDate.now();
        waterPlaceWeatherList.add(newWaterPlaceWeather(waterPlace, now));
        waterPlaceWeatherList.add(newWaterPlaceWeather(waterPlace, now.plusDays(1)));
        waterPlaceWeatherList.add(newWaterPlaceWeather(waterPlace, now.plusDays(2)));
        waterPlaceWeatherList.add(newWaterPlaceWeather(waterPlace, now.plusDays(3)));
        waterPlaceWeatherList.add(newWaterPlaceWeather(waterPlace, now.plusDays(4)));

        waterPlaceWeatherList.add(newWaterPlaceWeather(waterPlace3, now.minusDays(1)));
        waterPlaceWeatherList.add(newWaterPlaceWeather(waterPlace3, now));
        waterPlaceWeatherList.add(newWaterPlaceWeather(waterPlace3, now.plusDays(1)));
        waterPlaceWeatherList.add(newWaterPlaceWeather(waterPlace3, now.plusDays(2)));
        waterPlaceWeatherList.add(newWaterPlaceWeather(waterPlace3, now.plusDays(3)));
        waterPlaceWeatherRepository.saveAll(waterPlaceWeatherList);

        List<TripSchedule> tripScheduleList = new ArrayList<>();
        int year = now.getYear();
        int month = now.getMonthValue();
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.of(year, month, 1), 50));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.of(year, month, 3), 150));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.of(year, month, 5), 60));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.of(year, month, 7), 34));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.of(year, month, 9), 11));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.of(year, month, 11), 94));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.of(year, month, 15), 11));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.of(year, month, 16), 51));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.of(year, month, 18), 111));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.of(year, month, 20), 61));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.of(year, month, 22), 22));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.of(year, month, 24), 26));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.of(year, month, 27), 98));
        tripScheduleRepository.saveAll(tripScheduleList);

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(newReview(waterPlace, member2, "content2", 2, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewList.add(newReview(waterPlace, member3, "content3", 3, WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewList.add(newReview(waterPlace, member4, "content4", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewList.add(newReview(waterPlace, member6, "content6", 4, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewList.add(newReview(waterPlace, member1, "content1", 1, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        Review review6 = newReview(waterPlace, member5, "content5", 5, WaterQualityReviewEnum.DIRTY, waterPlaceRepository);
        reviewList.add(review6);
        reviewList.add(newReview(waterPlace2, member2, "content2", 2, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewList.add(newReview(waterPlace2, member3, "content3", 3, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewList.add(newReview(waterPlace2, member4, "content4", 4, WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewList.add(newReview(waterPlace2, member6, "content6", 4, WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewList.add(newReview(waterPlace2, member1, "content1", 1, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewList.add(newReview(waterPlace2, member5, "content5", 5, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.saveAll(reviewList);

        List<ReviewImage> reviewImageList = new ArrayList<>();
        reviewImageList.add(newReviewImage(review6, "storeFileUrl1"));
        reviewImageList.add(newReviewImage(review6, "storeFileUrl2"));
        reviewImageList.add(newReviewImage(review6, "storeFileUrl3"));
        reviewImageList.add(newReviewImage(review6, "storeFileUrl4"));
        reviewImageList.add(newReviewImage(review6, "storeFileUrl5"));
        reviewImageList.add(newReviewImage(review6, "storeFileUrl6"));
        reviewImageRepository.saveAll(reviewImageList);

        em.clear();
    }
}