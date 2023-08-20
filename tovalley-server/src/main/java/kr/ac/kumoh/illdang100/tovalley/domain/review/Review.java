package kr.ac.kumoh.illdang100.tovalley.domain.review;

import kr.ac.kumoh.illdang100.tovalley.domain.BaseTimeEntity;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "water_place_id", nullable = false)
    private WaterPlace waterPlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 256)
    private String reviewContent;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = 5)
    private WaterQualityReviewEnum waterQualityReview;
}
