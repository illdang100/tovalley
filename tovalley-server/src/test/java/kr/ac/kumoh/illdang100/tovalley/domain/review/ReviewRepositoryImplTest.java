package kr.ac.kumoh.illdang100.tovalley.domain.review;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.review.ReviewRespDto.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class ReviewRepositoryImplTest extends DummyObject {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private WaterPlaceRepository waterPlaceRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        autoIncrementReset();
        dataSetting();
        em.flush();
        em.clear();
    }

    @Test
    public void findReviewsByWaterPlaceId_최신순_test() {

        // given
        Long waterPlaceId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));

        // when
        List<WaterPlaceReviewRespDto> content =
                reviewRepository.findReviewsByWaterPlaceId(waterPlaceId, pageRequest)
                        .getContent();

        // then
        assertThat(content.size()).isEqualTo(5);
        assertThat(content.get(0).getReviewImages()).isEqualTo(null);
        assertThat(content.get(0).getNickname()).isEqualTo("member5");
        assertThat(content.get(1).getNickname()).isEqualTo("member1");
        assertThat(content.get(2).getNickname()).isEqualTo("member6");
        assertThat(content.get(3).getNickname()).isEqualTo("member4");
        assertThat(content.get(4).getNickname()).isEqualTo("member3");
    }

    @Test
    public void findReviewsByWaterPlaceId_평점높은순_test() {

        // given
        Long waterPlaceId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "rating"));

        // when
        List<WaterPlaceReviewRespDto> content =
                reviewRepository.findReviewsByWaterPlaceId(waterPlaceId, pageRequest)
                        .getContent();

        // then
        assertThat(content.size()).isEqualTo(5);
        assertThat(content.get(0).getReviewImages()).isEqualTo(null);
        assertThat(content.get(0).getNickname()).isEqualTo("member5");
        assertThat(content.get(1).getNickname()).isEqualTo("member6");
        assertThat(content.get(2).getNickname()).isEqualTo("member4");
        assertThat(content.get(3).getNickname()).isEqualTo("member3");
        assertThat(content.get(4).getNickname()).isEqualTo("member2");
    }

    @Test
    public void findReviewsByWaterPlaceId_평점낮은순_test() {

        // given
        Long waterPlaceId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "rating"));

        // when
        List<WaterPlaceReviewRespDto> content =
                reviewRepository.findReviewsByWaterPlaceId(waterPlaceId, pageRequest)
                        .getContent();

        // then
        assertThat(content.size()).isEqualTo(5);
        assertThat(content.get(0).getReviewImages()).isEqualTo(null);
        assertThat(content.get(0).getNickname()).isEqualTo("member1");
        assertThat(content.get(1).getNickname()).isEqualTo("member2");
        assertThat(content.get(2).getNickname()).isEqualTo("member3");
        assertThat(content.get(3).getNickname()).isEqualTo("member6");
        assertThat(content.get(4).getNickname()).isEqualTo("member4");
    }

    private void dataSetting() {

        Member member1 = memberRepository.save(newMember("kakao_1234", "member1"));
        Member member2 = memberRepository.save(newMember("kakao_5678", "member2"));
        Member member3 = memberRepository.save(newMember("kakao_9101", "member3"));
        Member member4 = memberRepository.save(newMember("kakao_1121", "member4"));
        Member member5 = memberRepository.save(newMember("kakao_3141", "member5"));
        Member member6 = memberRepository.save(newMember("kakao_4231", "member6"));
        WaterPlace waterPlace1 = waterPlaceRepository.save(newWaterPlace("서울계곡", "서울특별시", 4.9, 2010));
        WaterPlace waterPlace2 = waterPlaceRepository.save(newWaterPlace("서울계곡", "서울특별시", 4.9, 2010));

        reviewRepository.save(newReview(waterPlace1, member2, "content2", 2, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(waterPlace1, member3, "content3", 3, WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(waterPlace1, member4, "content4", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(newReview(waterPlace1, member6, "content6", 4, WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(waterPlace1, member1, "content1", 1, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(newReview(waterPlace1, member5, "content5", 5, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));

        reviewRepository.save(newReview(waterPlace2, member2, "content2", 2, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(waterPlace2, member3, "content3", 3, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(newReview(waterPlace2, member4, "content4", 4, WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(waterPlace2, member6, "content6", 4, WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(waterPlace2, member1, "content1", 1, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(newReview(waterPlace2, member5, "content5", 5, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
    }

    private void autoIncrementReset() {

        em.createNativeQuery("ALTER TABLE water_place ALTER COLUMN water_place_id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE member ALTER COLUMN member_id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE review ALTER COLUMN review_id RESTART WITH 1").executeUpdate();
    }
}