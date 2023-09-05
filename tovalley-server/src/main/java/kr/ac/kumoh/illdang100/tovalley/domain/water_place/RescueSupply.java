package kr.ac.kumoh.illdang100.tovalley.domain.water_place;

import kr.ac.kumoh.illdang100.tovalley.form.water_place.CreateWaterPlaceForm;
import kr.ac.kumoh.illdang100.tovalley.form.water_place.WaterPlaceForm;
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

    public void update(WaterPlaceForm form) {
        Integer lifeBoatNum = form.getLifeBoatNum();
        Integer portableStandNum = form.getPortableStandNum();
        Integer lifeJacketNum = form.getLifeJacketNum();
        Integer lifeRingNum = form.getLifeRingNum();
        Integer rescueRopeNum = form.getRescueRopeNum();
        Integer rescueRodNum = form.getRescueRodNum();

        this.lifeBoatNum = lifeBoatNum == null ? -1 : lifeBoatNum;
        this.portableStandNum = portableStandNum == null ? -1 : portableStandNum;
        this.lifeJacketNum = lifeJacketNum == null ? -1 : lifeJacketNum;
        this.lifeRingNum = lifeRingNum == null ? -1 : lifeRingNum;
        this.rescueRopeNum = rescueRopeNum == null ? -1 : rescueRopeNum;
        this.rescueRodNum = rescueRodNum == null ? -1 : rescueRodNum;
    }

    public static RescueSupply createNewRescueSupply(WaterPlace waterPlace, CreateWaterPlaceForm form) {

        Integer lifeBoatNum = form.getLifeBoatNum();
        Integer portableStandNum = form.getPortableStandNum();
        Integer lifeJacketNum = form.getLifeJacketNum();
        Integer lifeRingNum = form.getLifeRingNum();
        Integer rescueRopeNum = form.getRescueRopeNum();
        Integer rescueRodNum = form.getRescueRodNum();

        return RescueSupply.builder()
                .waterPlace(waterPlace)
                .lifeBoatNum(lifeBoatNum == null ? -1 : lifeBoatNum)
                .portableStandNum(portableStandNum == null ? -1 : portableStandNum)
                .lifeJacketNum(lifeJacketNum == null ? -1 : lifeJacketNum)
                .lifeRingNum(lifeRingNum == null ? -1 : lifeRingNum)
                .rescueRopeNum(rescueRopeNum == null ? -1 : rescueRopeNum)
                .rescueRodNum(rescueRodNum == null ? -1 : rescueRodNum)
                .build();
    }
}
