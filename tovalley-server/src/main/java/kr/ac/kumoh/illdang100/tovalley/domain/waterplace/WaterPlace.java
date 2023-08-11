package kr.ac.kumoh.illdang100.tovalley.domain.waterplace;

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

    @Column(length = 20)
    private String town; // 읍면

    @Column(length = 254)
    private String subLocation; // 세부지명

    @Column(nullable = false, length = 254)
    private String address; // 주소

    @Column(length = 20)
    private String waterPlaceCategory; // 구분(계곡, 하천)

    @Embedded
    private Coordinate coordinate; // 위경도 좌표

    @Column(nullable = false, length = 20)
    private String managementType; // 관리유형(일반지역, 중점관리지역)

    @Column(nullable = false)
    private Double rating;
}
