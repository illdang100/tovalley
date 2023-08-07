package kr.ac.kumoh.illdang100.tovalley.domain.rescue_supply;

import kr.ac.kumoh.illdang100.tovalley.domain.waterplace.WaterPlace;
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

    private Integer lifeBoatNum;

    private Integer lifeJacketNum;

    private Integer lifeRopeNum;

    private Integer lifeRingNum;

    private Integer lifeBuoyNum;

    private Integer movableCradleNum;
}
