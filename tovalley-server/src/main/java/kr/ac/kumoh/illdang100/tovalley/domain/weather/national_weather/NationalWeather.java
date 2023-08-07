package kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather;

import kr.ac.kumoh.illdang100.tovalley.domain.BaseTimeEntity;
import kr.ac.kumoh.illdang100.tovalley.domain.national_region.NationalRegion;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.Climate;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NationalWeather extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "national_weather_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "national_region_id", nullable = false)
    private NationalRegion nationalRegion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private Climate climate;

    @Column(nullable = false)
    private Double lowestTemperature;

    @Column(nullable = false)
    private Double highestTemperature;

    @Column(nullable = false)
    private LocalDate weatherDate;
}
