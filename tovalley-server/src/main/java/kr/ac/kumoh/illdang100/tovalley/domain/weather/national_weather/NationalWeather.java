package kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "NationalWeather")
public class NationalWeather {

    @Id
    private String id;

    @Indexed
    private String regionName;

    @Indexed
    private LocalDate weatherDate; // 날짜

    private String climate; // 날씨 (비, 눈, 구름 등)

    private String climateIcon; // 날씨 아이콘 ID

    private String climateDescription; // 날씨 설명

    private Double lowestTemperature; // 최소 일일 온도

    private Double highestTemperature; // 최고 일일 온도

    private Double rainPrecipitation; // 강수량(mm)

    private Integer humidity; // 습도(%)

    private Double windSpeed; // 풍속(m/s)

    private Integer clouds; // 흐림(%)

    private Double dayFeelsLike; // 주간 체감 온도
}