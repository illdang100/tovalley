package kr.ac.kumoh.illdang100.tovalley.domain.medical_facility;

import kr.ac.kumoh.illdang100.tovalley.domain.Coordinate;
import kr.ac.kumoh.illdang100.tovalley.domain.waterplace.WaterPlace;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MedicalFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_facility_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "water_place_id")
    private WaterPlace waterPlace;

    @Column(nullable = false, length=20)
    private String facilityName;

    @Embedded
    private Coordinate coordinate;

    @Column(length = 15)
    private String tel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private FacilityType facilityType;
}
