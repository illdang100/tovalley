package kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SpecialWeatherEnum {

    BREAKING("특보"), PRELIMINARY("예비특보");
    private String value;
}
