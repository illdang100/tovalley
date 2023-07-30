package kr.ac.kumoh.illdang100.tovalley.domain.weather.valley_weather;

import kr.ac.kumoh.illdang100.tovalley.domain.valley.Valley;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.Climate;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ValleyWeather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "valley_weather_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "valley_id")
    private Valley valley;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private Climate climate;

    private Double lowestTemperature;

    private Double highestTemperature;

    @Column(nullable = false)
    private LocalDate weatherDate;
}
