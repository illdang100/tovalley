package kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather;

import java.util.List;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash(value = "SpecialWeather")
public class SpecialWeather {

    @Id
    private String id;

    private LocalDateTime announcementTime;

    private LocalDateTime effectiveTime;

    @Enumerated(EnumType.STRING)
    private WeatherAlertType weatherAlertType;

    @Enumerated(EnumType.STRING)
    @Indexed
    private SpecialWeatherEnum category;

    private String title;

    private List<SpecialWeatherDetail> details;
}