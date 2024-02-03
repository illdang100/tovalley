package kr.ac.kumoh.illdang100.tovalley.web.page;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.*;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripScheduleRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.*;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.water_place_weather.WaterPlaceWeatherRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
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
public class MyPageApiControllerTest extends DummyObject {

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
    @WithUserDetails(value = "kakao_1234", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void getMyPage_test() throws Exception {

        // given
//        PageRequest pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));

        // when
        ResultActions resultActions = mvc.perform(get("/api/auth/my-page"));

        LocalDate now = LocalDate.now();

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userProfile.memberNick").value("member1"))
                .andExpect(jsonPath("$.data.myReviews.content.size()").value(2))
                .andExpect(jsonPath("$.data.myReviews.content[0].reviewId").value(11))
                .andExpect(jsonPath("$.data.myUpcomingTripSchedules.size()").value(3))
                .andExpect(jsonPath("$.data.myUpcomingTripSchedules[0].tripScheduleId").value(13))
                .andExpect(jsonPath("$.data.myUpcomingTripSchedules[1].tripScheduleId").value(14))
                .andExpect(jsonPath("$.data.myUpcomingTripSchedules[2].tripScheduleId").value(15))
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
        WaterPlace waterPlace1 = newWaterPlace("서울계곡", "서울특별시", 0.0, 0);
        WaterPlace waterPlace2 = newWaterPlace("금오계곡", "경상북도", 0.0, 0);
        WaterPlace waterPlace3 = newWaterPlace("울릉계곡", "경상북도", 0.0, 0);
        waterPlaceList.add(waterPlace1);
        waterPlaceList.add(waterPlace2);
        waterPlaceList.add(waterPlace3);
        waterPlaceRepository.saveAll(waterPlaceList);

        List<WaterPlaceDetail> waterPlaceDetailList = new ArrayList<>();
        waterPlaceDetailList.add(newWaterPlaceDetail(waterPlace1));
        waterPlaceDetailList.add(newWaterPlaceDetail(waterPlace2));
        waterPlaceDetailList.add(newWaterPlaceDetail(waterPlace3));
        waterPlaceDetailRepository.saveAll(waterPlaceDetailList);

        List<RescueSupply> rescueSupplyList = new ArrayList<>();
        rescueSupplyList.add(newRescueSupply(waterPlace1));
        rescueSupplyList.add(newRescueSupply(waterPlace2));
        rescueSupplyList.add(newRescueSupply(waterPlace3));
        rescueSupplyRepository.saveAll(rescueSupplyList);

        LocalDate now = LocalDate.now();

        List<TripSchedule> tripScheduleList = new ArrayList<>();
        int year = now.getYear();
        int month = now.getMonthValue();
        TripSchedule tripSchedule1 = newTripSchedule(member2, waterPlace1, now.minusDays(1), 50);
        TripSchedule tripSchedule2 = newTripSchedule(member3, waterPlace1, now.minusDays(3), 150);
        TripSchedule tripSchedule3 = newTripSchedule(member4, waterPlace1, now.minusDays(5), 60);
        TripSchedule tripSchedule4 = newTripSchedule(member6, waterPlace1, now.minusDays(3), 34);
        TripSchedule tripSchedule5 = newTripSchedule(member1, waterPlace1, now.minusDays(7), 11);
        TripSchedule tripSchedule6 = newTripSchedule(member5, waterPlace1, now.minusDays(11), 94);

        TripSchedule tripSchedule7 = newTripSchedule(member2, waterPlace2, now.minusDays(1), 11);
        TripSchedule tripSchedule8 = newTripSchedule(member3, waterPlace2, now.minusDays(3), 51);
        TripSchedule tripSchedule9 = newTripSchedule(member4, waterPlace2, now.minusDays(5), 111);
        TripSchedule tripSchedule10 = newTripSchedule(member6, waterPlace2, now.minusDays(3), 61);
        TripSchedule tripSchedule11 = newTripSchedule(member1, waterPlace2, now.minusDays(7), 22);
        TripSchedule tripSchedule12 = newTripSchedule(member6, waterPlace2, now.minusDays(11), 26);

        TripSchedule tripSchedule13 = newTripSchedule(member1, waterPlace1, now.plusDays(10), 61);
        TripSchedule tripSchedule14 = newTripSchedule(member1, waterPlace2, now.plusMonths(1), 22);
        TripSchedule tripSchedule15 = newTripSchedule(member1, waterPlace3, now.plusYears(1), 26);
        tripScheduleList.add(tripSchedule1);
        tripScheduleList.add(tripSchedule2);
        tripScheduleList.add(tripSchedule3);
        tripScheduleList.add(tripSchedule4);
        tripScheduleList.add(tripSchedule5);
        tripScheduleList.add(tripSchedule6);
        tripScheduleList.add(tripSchedule7);
        tripScheduleList.add(tripSchedule8);
        tripScheduleList.add(tripSchedule9);
        tripScheduleList.add(tripSchedule10);
        tripScheduleList.add(tripSchedule11);
        tripScheduleList.add(tripSchedule12);

        tripScheduleList.add(tripSchedule13);
        tripScheduleList.add(tripSchedule14);
        tripScheduleList.add(tripSchedule15);
        tripScheduleRepository.saveAll(tripScheduleList);

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(newReview(waterPlace1, tripSchedule1, "content2", 2, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewList.add(newReview(waterPlace1, tripSchedule2, "content3", 3, WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewList.add(newReview(waterPlace1, tripSchedule3, "content4", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewList.add(newReview(waterPlace1, tripSchedule4, "content6", 4, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewList.add(newReview(waterPlace1, tripSchedule5, "content1", 1, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        Review review6 = newReview(waterPlace1, tripSchedule6, "content5", 5, WaterQualityReviewEnum.DIRTY, waterPlaceRepository);
        reviewList.add(review6);
        reviewList.add(newReview(waterPlace2, tripSchedule7, "content2", 2, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewList.add(newReview(waterPlace2, tripSchedule8, "content3", 3, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewList.add(newReview(waterPlace2, tripSchedule9, "content4", 4, WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewList.add(newReview(waterPlace2, tripSchedule10, "content6", 4, WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewList.add(newReview(waterPlace2, tripSchedule11, "content1", 1, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewList.add(newReview(waterPlace2, tripSchedule12, "content5", 5, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
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
