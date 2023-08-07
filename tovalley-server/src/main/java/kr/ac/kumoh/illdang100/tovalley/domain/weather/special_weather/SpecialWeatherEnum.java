package kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SpecialWeatherEnum {

    STRONGWIND("강풍"), WIND("풍랑"), HEAVYRAIN("호우"), HEAVYSNOW("대설"), DRY("건조"), STORMSURGE("폭풍해일"),
    COLDWAVE("한파"), TYPHOON("태풍"), YELLOWSAND("황사"), HEATWAVE("폭염");
    private String value;
}
