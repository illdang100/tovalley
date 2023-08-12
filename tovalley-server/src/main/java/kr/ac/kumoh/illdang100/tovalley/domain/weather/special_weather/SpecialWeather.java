package kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SpecialWeather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "special_weather_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime announcementTime;

    @Column(nullable = false)
    private LocalDateTime effectiveTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 11)
    private WeatherAlertType weatherAlertType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 11)
    private SpecialWeatherEnum category;

    @Column(nullable = false, length = 20)
    private String title;
}
