package kr.ac.kumoh.illdang100.tovalley.domain.water_place;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WaterPlaceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "water_place_detail_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "water_place_id", nullable = false)
    private WaterPlace waterPlace;

    @Column(nullable = false, length = 20)
    private String waterPlaySegment; // 물놀이구간(m)

    @Column(nullable = false, length = 20)
    private String deepestDepth; // 수심(깊은곳)

    @Column(nullable = false, length = 20)
    private String avgDepth; // 평균 수심

    @Column(length = 20)
    private String annualVisitors; // 연평균 총 이용객수(천명)

    @Column(length = 20)
    private String dangerSegments; // 위험구역구간

    @Column(length = 20)
    private String dangerSignboardsNum; // 위험구역 설정 안내표지판(합계)

    @Column(length = 254)
    private String safetyMeasures; // 안전조치 사항
}
