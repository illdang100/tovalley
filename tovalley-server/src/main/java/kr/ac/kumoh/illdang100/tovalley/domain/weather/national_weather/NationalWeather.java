package kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "national_region_id", nullable = false)
    private NationalRegion nationalRegion;

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
    private Double rainPrecipitation; // 강수량(mm)
}
