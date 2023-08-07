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
    @Column(name = "water_place_weather_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "water_place_id", nullable = false)
    private WaterPlace waterPlace;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private Climate climate;

    @Column(nullable = false, length = 3)
    private String climateIcon;

    @Column(nullable = false, length = 20)
    private String climateDescription;

    @Column(nullable = false)
    private Double lowestTemperature;

    @Column(nullable = false)
    private Double highestTemperature;

    @Column(nullable = false)
    private LocalDate weatherDate;

    @Column(nullable = false)
    private Integer humidity;

    @Column(nullable = false)
    private Double windSpeed;

    @Column(nullable = false)
    private Double precipitation;
}
