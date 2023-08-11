package kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WeatherAlertType {

    WINDSTORM("강풍"), HEAVY_RAIN("호우"), COLD_WAVE("한파"), DROUGHT("건조"),
    STORM_SURGE("폭풍해일"), ROUGH_SEA("풍랑"), TYPHOON("태풍"), HEAVY_SNOW("대설"),
    YELLOW_DUST("황사"), HEAT_WAVE("폭염"), ETC("기타");
    private String value;
}
