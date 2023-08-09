package kr.ac.kumoh.illdang100.tovalley.domain.weather.water_place_weather;

import kr.ac.kumoh.illdang100.tovalley.domain.BaseTimeEntity;
import kr.ac.kumoh.illdang100.tovalley.domain.waterplace.WaterPlace;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WaterPlaceWeather extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "water_place_weather_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "water_place_id", nullable = false)
    private WaterPlace waterPlace;

    @Column(nullable = false, length = 12)
    private String climate; // 날씨 (비, 눈, 구름 등)

    @Column(nullable = false, length = 3)
    private String climateIcon; // 날씨 아이콘 ID

    @Column(nullable = false, length = 20)
    private String climateDescription; // 날씨 설명

    @Column(nullable = false)
    private Double lowestTemperature; // 최소 일일 온도

    @Column(nullable = false)
    private Double highestTemperature; // 최고 일일 온도

    @Column(nullable = false)
    private LocalDate weatherDate; // 날짜

    @Column(nullable = false)
    private Integer humidity; // 습도(%)

    @Column(nullable = false)
    private Double windSpeed; // 풍속(m/s)

    @Column(nullable = false)
    private Double rainPrecipitation; // 강수량(mm)

    @Column(nullable = false)
    private Integer clouds; // 흐림(%)

    @Column(nullable = false)
    private Double dayFeelsLike; // 주간 체감 온도
}