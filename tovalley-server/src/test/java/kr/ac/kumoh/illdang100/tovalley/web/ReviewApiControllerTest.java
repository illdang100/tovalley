package kr.ac.kumoh.illdang100.tovalley.web;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.*;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripScheduleRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.*;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
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
import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.util.TokenUtil.createTestAccessToken;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Sql("classpath:db/teardown.sql")
class ReviewApiControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private WaterPlaceRepository waterPlaceRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private TripScheduleRepository tripScheduleRepository;
    @Autowired
    private ReviewImageRepository reviewImageRepository;
    @Autowired
    private JwtProcess jwtProcess;
    @Autowired
    private EntityManager em;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        dataSetting();
        accessToken = createTestAccessToken(memberRepository, jwtProcess, "kakao_1234");
    }

    @Test
    public void getWaterPlaceReviews_success_test() throws Exception {

        // given
        Long waterPlaceId = 1L;

        // when
        ResultActions resultActions =
                mvc.perform(get("/api/auth/water-places/" + waterPlaceId + "/reviews?sort=createdDate,desc&page=0&size=5")
                        .cookie(new Cookie(JwtVO.ACCESS_TOKEN, accessToken)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.waterPlaceRating").value(3.2))
                .andExpect(jsonPath("$.data.reviewCnt").value(6))
                .andExpect(jsonPath("$.data.ratingRatio['1']").value(1))
                .andExpect(jsonPath("$.data.ratingRatio['2']").value(1))
                .andExpect(jsonPath("$.data.ratingRatio['3']").value(1))
                .andExpect(jsonPath("$.data.ratingRatio['4']").value(2))
                .andExpect(jsonPath("$.data.ratingRatio['5']").value(1))
                .andExpect(jsonPath("$.data.reviews.content.size()").value(5))
                .andExpect(jsonPath("$.data.reviews.content[0].reviewId").value(6))
                .andExpect(jsonPath("$.data.reviews.content[0].reviewImages.size()").value(6))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getWaterPlaceReviews_failure_test() throws Exception {

        // given
        Long waterPlaceId = 4000L;

        // when
        ResultActions resultActions =
                mvc.perform(get("/api/auth/water-places/" + waterPlaceId + "/reviews?sort=createdDate,desc&page=0&size=5")
                        .cookie(new Cookie(JwtVO.ACCESS_TOKEN, accessToken)));

        // then
        resultActions
                .andExpect(status().isBadRequest())
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
        waterPlaceList.add(waterPlace);
        waterPlaceRepository.saveAll(waterPlaceList);

        LocalDate now = LocalDate.now();

        List<TripSchedule> tripScheduleList = new ArrayList<>();
        TripSchedule tripSchedule1 = newTripSchedule(member2, waterPlace, now, 10);
        TripSchedule tripSchedule2 = newTripSchedule(member3, waterPlace, now, 10);
        TripSchedule tripSchedule3 = newTripSchedule(member4, waterPlace, now, 10);
        TripSchedule tripSchedule4 = newTripSchedule(member6, waterPlace, now, 10);
        TripSchedule tripSchedule5 = newTripSchedule(member1, waterPlace, now, 10);
        TripSchedule tripSchedule6 = newTripSchedule(member5, waterPlace, now, 10);
        tripScheduleList.add(tripSchedule1);
        tripScheduleList.add(tripSchedule2);
        tripScheduleList.add(tripSchedule3);
        tripScheduleList.add(tripSchedule4);
        tripScheduleList.add(tripSchedule5);
        tripScheduleList.add(tripSchedule6);
        tripScheduleRepository.saveAll(tripScheduleList);

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(newReview(waterPlace, tripSchedule1, "content2", 2, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewList.add(newReview(waterPlace, tripSchedule2, "content3", 3, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewList.add(newReview(waterPlace, tripSchedule3, "content4", 4, WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewList.add(newReview(waterPlace, tripSchedule4, "content6", 4, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewList.add(newReview(waterPlace, tripSchedule5, "content1", 1, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        Review review6 = newReview(waterPlace, tripSchedule6, "content5", 5, WaterQualityReviewEnum.DIRTY, waterPlaceRepository);
        reviewList.add(review6);
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