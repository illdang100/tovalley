package kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SpecialWeatherEnum {

    HEATWAVE("폭염"), COLDWAVE("한파"), HEAVYRAIN("호우"), HEAVYSNOW("대설"), TYPHOON("태풍"),
    AIRPOLLUTION("대기오염"), STRONGWIND("강풍"), FINEDUST("미세먼지");
    private String value;
}
