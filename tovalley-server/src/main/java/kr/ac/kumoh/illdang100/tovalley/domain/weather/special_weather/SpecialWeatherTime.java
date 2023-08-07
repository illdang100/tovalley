package kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SpecialWeatherTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "special_weather_time_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime announcementTime;

    @Column(nullable = false)
    private LocalDateTime effectiveTime;
}
