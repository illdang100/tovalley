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

    @Enumerated(EnumType.STRING)
    private SpecialWeatherEnum type;

    private String title;

    private LocalDateTime announcementDate;

    private LocalDateTime effectiveDate;

    private String content;
}
