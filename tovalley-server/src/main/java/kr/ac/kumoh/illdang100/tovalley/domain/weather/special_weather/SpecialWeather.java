package kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather;

import lombok.*;

import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "special_weather_time_id", nullable = false)
    private SpecialWeatherTime specialWeatherTime;

    @Enumerated(EnumType.STRING)
    private SpecialWeatherEnum type;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 256)
    private String content;
}
