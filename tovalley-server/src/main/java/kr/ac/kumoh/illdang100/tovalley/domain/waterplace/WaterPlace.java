package kr.ac.kumoh.illdang100.tovalley.domain.waterplace;

import kr.ac.kumoh.illdang100.tovalley.domain.BaseTimeEntity;
import kr.ac.kumoh.illdang100.tovalley.domain.Coordinate;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WaterPlace extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "water_place_id")
    private Long id;

    @Column(nullable = false, length = 10)
    private String province;

    @Column(nullable = false, length = 10)
    private String waterPlaceName;

    private Double waterQuality;

    private Double deepestDepth;

    private Double averageDepth;

    @Column(nullable = false, length = 25)
    private String waterPlaceAddress;

    @Embedded
    private Coordinate coordinate;
}
