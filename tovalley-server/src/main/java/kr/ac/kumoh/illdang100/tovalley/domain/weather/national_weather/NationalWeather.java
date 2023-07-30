package kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather;

import kr.ac.kumoh.illdang100.tovalley.domain.Coordinate;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.Climate;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NationalWeather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "national_weather_id")
    private Long id;

    private String regionName;

    @Embedded
    private Coordinate coordinate;

    @Enumerated(EnumType.STRING)
    private Climate climate;

    private Double lowestTemperature;

    private Double highestTemperature;

    private LocalDate weatherDate;
}
