package kr.ac.kumoh.illdang100.tovalley.dummy;

import kr.ac.kumoh.illdang100.tovalley.domain.accident.Accident;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.Review;
import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewImageRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.WaterQualityReviewEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripScheduleRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.service.OpenApiServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class DummyDevInit extends DummyObject {

    @Profile("dev") // prod 모드에서는 실행되면 안된다.
    @Bean
    CommandLineRunner devInit(OpenApiServiceImpl openApiService, WaterPlaceRepository waterPlaceRepository,
                              AccidentRepository accidentRepository, TripScheduleRepository tripScheduleRepository,
                              MemberRepository memberRepository, ReviewRepository reviewRepository,
                              ReviewImageRepository reviewImageRepository) {
        return (args) -> {
            // 서버 실행시 무조건 실행된다.

            openApiService.fetchAndSaveWaterPlacesData();
            openApiService.fetchAndSaveSpecialWeatherData();
            openApiService.fetchAndSaveNationalWeatherData();

            /* 더미 데이터(주석 해제 후 서버 실행하고 반드시 다시 주석처리하기!!)  */
            /*LocalDate now = LocalDate.now();

            WaterPlace a = waterPlaceRepository.findById(1000L).get();
            WaterPlace b = waterPlaceRepository.findById(1001L).get();
            WaterPlace c = waterPlaceRepository.findById(1002L).get();
            WaterPlace d = waterPlaceRepository.findById(1003L).get();
            WaterPlace e = waterPlaceRepository.findById(1004L).get();

            waterPlaceRepository.save(a);
            waterPlaceRepository.save(b);
            waterPlaceRepository.save(c);
            waterPlaceRepository.save(d);
            waterPlaceRepository.save(e);

            accidentRepository.save(Accident.builder()
                    .waterPlace(a)
                    .accidentDate(LocalDate.now().minusMonths(5))
                    .accidentCondition(AccidentEnum.INJURY)
                    .peopleNum(2)
                    .build());

            accidentRepository.save(Accident.builder()
                    .waterPlace(a)
                    .accidentDate(LocalDate.now().minusMonths(48))
                    .accidentCondition(AccidentEnum.INJURY)
                    .peopleNum(2)
                    .build());

            accidentRepository.save(Accident.builder()
                    .waterPlace(a)
                    .accidentDate(LocalDate.now().minusMonths(3))
                    .accidentCondition(AccidentEnum.DEATH)
                    .peopleNum(100)
                    .build());

            accidentRepository.save(Accident.builder()
                    .waterPlace(a)
                    .accidentDate(LocalDate.now().minusMonths(12))
                    .accidentCondition(AccidentEnum.DEATH)
                    .peopleNum(100)
                    .build());

            accidentRepository.save(Accident.builder()
                    .waterPlace(a)
                    .accidentDate(LocalDate.now().minusMonths(62))
                    .accidentCondition(AccidentEnum.DEATH)
                    .peopleNum(100)
                    .build());

            accidentRepository.save(Accident.builder()
                    .waterPlace(a)
                    .accidentDate(LocalDate.now().minusMonths(1))
                    .accidentCondition(AccidentEnum.DISAPPEARANCE)
                    .peopleNum(66)
                    .build());

            Member member1 = memberRepository.save(newMember("kakao_1234", "member1"));
            Member member2 = memberRepository.save(newMember("kakao_5678", "member2"));
            Member member3 = memberRepository.save(newMember("kakao_9101", "member3"));
            Member member4 = memberRepository.save(newMember("kakao_1121", "member4"));
            Member member5 = memberRepository.save(newMember("kakao_3141", "member5"));
            Member member6 = memberRepository.save(newMember("kakao_4231", "member6"));

            List<TripSchedule> tripScheduleList = new ArrayList<>();
            TripSchedule tripSchedule1 = newTripSchedule(member2, a, now, 10);
            TripSchedule tripSchedule2 = newTripSchedule(member3, a, now, 10);
            TripSchedule tripSchedule3 = newTripSchedule(member4, a, now, 10);
            TripSchedule tripSchedule4 = newTripSchedule(member6, a, now, 10);
            TripSchedule tripSchedule5 = newTripSchedule(member1, a, now.minusDays(5), 10);
            TripSchedule tripSchedule6 = newTripSchedule(member5, a, now, 10);

            TripSchedule tripSchedule7 = newTripSchedule(member2, b, now, 10);
            TripSchedule tripSchedule8 = newTripSchedule(member3, b, now, 10);
            TripSchedule tripSchedule9 = newTripSchedule(member4, b, now, 10);
            TripSchedule tripSchedule10 = newTripSchedule(member6, b, now, 10);
            TripSchedule tripSchedule11 = newTripSchedule(member1, b, now, 10);
            TripSchedule tripSchedule12 = newTripSchedule(member5, b, now, 10);

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
            tripScheduleRepository.saveAll(tripScheduleList);

            reviewRepository.save(newReview(a, tripSchedule1, "content2", 2, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
            reviewRepository.save(newReview(a, tripSchedule2, "content3", 3, WaterQualityReviewEnum.FINE, waterPlaceRepository));
            reviewRepository.save(newReview(a, tripSchedule3, "content4", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
            reviewRepository.save(newReview(a, tripSchedule4, "content6", 4,WaterQualityReviewEnum.DIRTY , waterPlaceRepository));
            reviewRepository.save(newReview(a, tripSchedule5, "content1", 1, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
            Review review6 = reviewRepository.save(newReview(a, tripSchedule6, "content5", 5, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));

            reviewImageRepository.save(newReviewImage(review6, "storeFileUrl1"));
            reviewImageRepository.save(newReviewImage(review6, "storeFileUrl2"));
            reviewImageRepository.save(newReviewImage(review6, "storeFileUrl3"));
            reviewImageRepository.save(newReviewImage(review6, "storeFileUrl4"));
            reviewImageRepository.save(newReviewImage(review6, "storeFileUrl5"));
            reviewImageRepository.save(newReviewImage(review6, "storeFileUrl6"));

            reviewRepository.save(newReview(b, tripSchedule7, "content2", 3, WaterQualityReviewEnum.FINE, waterPlaceRepository));
            reviewRepository.save(newReview(b, tripSchedule8, "content3", 5, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
            reviewRepository.save(newReview(b, tripSchedule9, "content4", 5, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
            reviewRepository.save(newReview(b, tripSchedule10, "content6", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
            reviewRepository.save(newReview(b, tripSchedule11, "content1", 1, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
            reviewRepository.save(newReview(b, tripSchedule12, "content5", 5, WaterQualityReviewEnum.FINE, waterPlaceRepository));*/
        };
    }

    @Profile("prod")
    @Bean
    CommandLineRunner prodInit() {
        return (args) -> {
            // 서버 실행시 무조건 실행된다.

        };
    }
}
