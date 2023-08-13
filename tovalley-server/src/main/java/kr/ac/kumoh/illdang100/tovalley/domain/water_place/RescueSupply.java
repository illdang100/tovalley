package kr.ac.kumoh.illdang100.tovalley.domain.water_place;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RescueSupply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rescue_supply_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "water_place_id", nullable = false)
    private WaterPlace waterPlace;

    private Integer lifeBoatNum; // 인명구조함
    private Integer portableStandNum; // 이동식거치대
    private Integer lifeJacketNum; // 구명조끼
    private Integer lifeRingNum; // 구명환
    private Integer rescueRopeNum; // 구명로프
    private Integer rescueRodNum; // 구조봉
}
