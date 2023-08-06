package kr.ac.kumoh.illdang100.tovalley.domain.weather.water_place_weather;

import kr.ac.kumoh.illdang100.tovalley.domain.waterplace.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.Climate;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WaterPlaceWeather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "valley_weather_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "water_place_id")
    private WaterPlace waterPlace;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private Climate climate;

    private Double lowestTemperature;

    private Double highestTemperature;

    @Column(nullable = false)
    private LocalDate weatherDate;
}
