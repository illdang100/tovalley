package kr.ac.kumoh.illdang100.tovalley.domain.water_place;

import kr.ac.kumoh.illdang100.tovalley.domain.Coordinate;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WaterPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "water_place_id")
    private Long id;

    @Column(nullable = false, length = 254)
    private String waterPlaceName; // 물놀이 지역 명칭

    @Column(nullable = false, length = 20)
    private String province; // 시도

    @Column(nullable = false, length = 20)
    private String city; // 시군구

    @Column(nullable = false, length = 20)
    private String town; // 읍면

    @Column(nullable = false, length = 254)
    private String subLocation; // 세부지명

    @Column(nullable = false, length = 254)
    private String address; // 주소

    @Column(nullable = false, length = 20)
    private String waterPlaceCategory; // 구분(계곡, 하천)

    @Embedded
    private Coordinate coordinate; // 위경도 좌표

    @Column(nullable = false, length = 20)
    private String managementType; // 관리유형(일반지역, 중점관리지역)

    @Column(nullable = false)
    private Double rating; // 평점

    @Column(nullable = false)
    private int reviewCount; // 리뷰 개수

    public void calculateRating(Double rating) {
        double sum = rating * reviewCount;
        this.reviewCount = reviewCount + 1;
        this.rating = sum / reviewCount;
    }
}
