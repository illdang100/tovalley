package kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather;

import kr.ac.kumoh.illdang100.tovalley.domain.Coordinate;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NationalRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "national_region_id")
    private Long id;

    @Column(nullable = false, length = 6)
    private String regionName;

    @Embedded
    private Coordinate coordinate;
}
