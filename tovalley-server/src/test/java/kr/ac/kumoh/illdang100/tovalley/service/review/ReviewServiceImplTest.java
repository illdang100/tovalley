package kr.ac.kumoh.illdang100.tovalley.service.review;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.WaterQualityReviewEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripScheduleRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import kr.ac.kumoh.illdang100.tovalley.service.S3Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static kr.ac.kumoh.illdang100.tovalley.dto.review.ReviewReqDto.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest extends DummyObject {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private WaterPlaceRepository waterPlaceRepository;
    @Mock
    private TripScheduleRepository tripScheduleRepository;
    @Mock
    private S3Service s3Service;

    @Test
    public void writeReview_validation_tripDate_test() {

        // given
        Long memberId = 1L;
        AddNewReviewReqDto addNewReviewReqDto = new AddNewReviewReqDto(1L, WaterQualityReviewEnum.DIRTY, 3, "content", null);

        Member member = newMockMember(memberId, "kakao_1234", "nick", MemberEnum.CUSTOMER);
        WaterPlace waterPlace = newMockWaterPlace(1L, "금오계곡", "경상북도", 0.0, 0);

        TripSchedule tripSchedule = TripSchedule.builder()
                .member(member)
                .waterPlace(waterPlace)
                .tripDate(LocalDate.now())
                .tripNumber(3)
                .build();

        LocalDate now = LocalDate.now();

        // stub
        when(tripScheduleRepository.findById(any())).thenReturn(Optional.of(tripSchedule));
        when(waterPlaceRepository.findById(any())).thenReturn(Optional.of(waterPlace));

        // when

        // then
        assertThatThrownBy(() -> reviewService.writeReview(memberId, addNewReviewReqDto))
                .isInstanceOf(CustomApiException.class)
                .hasMessageContaining("현재 날짜(" + now + ") 이후 일정에 대해 리뷰를 작성할 수 없습니다");
    }

    @Test
    public void writeReview_validation_duplicate_review_test() {

        // given
        Long memberId = 1L;
        AddNewReviewReqDto addNewReviewReqDto = new AddNewReviewReqDto(1L, WaterQualityReviewEnum.DIRTY, 3, "content", null);

        Member member = newMockMember(memberId, "kakao_1234", "nick", MemberEnum.CUSTOMER);
        WaterPlace waterPlace = newMockWaterPlace(1L, "금오계곡", "경상북도", 0.0, 0);

        TripSchedule tripSchedule = TripSchedule.builder()
                .member(member)
                .waterPlace(waterPlace)
                .tripDate(LocalDate.now().minusDays(5))
                .tripNumber(3)
                .build();

        LocalDate now = LocalDate.now();

        // stub
        when(tripScheduleRepository.findById(any())).thenReturn(Optional.of(tripSchedule));
        when(waterPlaceRepository.findById(any())).thenReturn(Optional.of(waterPlace));
        when(reviewRepository.existsByTripScheduleId(any())).thenReturn(true);

        // when

        // then
        assertThatThrownBy(() -> reviewService.writeReview(memberId, addNewReviewReqDto))
                .isInstanceOf(CustomApiException.class)
                .hasMessageContaining("하나의 여행 일정에는 하나의 리뷰만 작성할 수 있습니다");
    }
}