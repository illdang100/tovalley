package kr.ac.kumoh.illdang100.tovalley.dummy;

import kr.ac.kumoh.illdang100.tovalley.domain.accident.Accident;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.Review;
import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewImageRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewRepository;
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

            /*WaterPlace a = waterPlaceRepository.findById(1000L).get();
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

            reviewRepository.save(newReview(a, member2, "content2", 2, waterPlaceRepository));
            reviewRepository.save(newReview(a, member3, "content3", 3, waterPlaceRepository));
            reviewRepository.save(newReview(a, member4, "content4", 4, waterPlaceRepository));
            reviewRepository.save(newReview(a, member6, "content6", 4, waterPlaceRepository));
            reviewRepository.save(newReview(a, member1, "content1", 1, waterPlaceRepository));
            Review review6 = reviewRepository.save(newReview(a, member5, "content5", 5, waterPlaceRepository));

            reviewImageRepository.save(newReviewImage(review6, "storeFileUrl1"));
            reviewImageRepository.save(newReviewImage(review6, "storeFileUrl2"));
            reviewImageRepository.save(newReviewImage(review6, "storeFileUrl3"));
            reviewImageRepository.save(newReviewImage(review6, "storeFileUrl4"));
            reviewImageRepository.save(newReviewImage(review6, "storeFileUrl5"));
            reviewImageRepository.save(newReviewImage(review6, "storeFileUrl6"));

            reviewRepository.save(newReview(b, member2, "content2", 3, waterPlaceRepository));
            reviewRepository.save(newReview(b, member3, "content3", 3, waterPlaceRepository));
            reviewRepository.save(newReview(b, member4, "content4", 3, waterPlaceRepository));
            reviewRepository.save(newReview(b, member6, "content6", 4, waterPlaceRepository));
            reviewRepository.save(newReview(b, member1, "content1", 1, waterPlaceRepository));
            reviewRepository.save(newReview(b, member5, "content5", 5, waterPlaceRepository));

            tripScheduleRepository.save(TripSchedule.builder()
                    .member(member1)
                    .waterPlace(a)
                    .tripDate(LocalDate.now().minusDays(15))
                    .tripNumber(16)
                    .build());

            tripScheduleRepository.save(TripSchedule.builder()
                    .member(member1)
                    .waterPlace(a)
                    .tripDate(LocalDate.now().minusDays(12))
                    .tripNumber(5)
                    .build());

            tripScheduleRepository.save(TripSchedule.builder()
                    .member(member1)
                    .waterPlace(a)
                    .tripDate(LocalDate.now().minusDays(9))
                    .tripNumber(11)
                    .build());

            tripScheduleRepository.save(TripSchedule.builder()
                    .member(member1)
                    .waterPlace(a)
                    .tripDate(LocalDate.now().minusDays(3))
                    .tripNumber(10)
                    .build());

            tripScheduleRepository.save(newTripSchedule(member1, a, LocalDate.now().minusMonths(1), 50));*/
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
