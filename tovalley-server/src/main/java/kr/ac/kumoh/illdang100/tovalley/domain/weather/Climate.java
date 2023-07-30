package kr.ac.kumoh.illdang100.tovalley.domain.weather;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Climate {

    CLEAR("맑"), FADE("흐림"), RAIN("비"), SNOW("눈"), RAINANDSNOW("비/눈"),
    SHOWER("소나기"), THUNDERSTORM("뇌우"), FOG("안개"), HEATWAVE("폭염"), COLDWAVE("한파");
    private String value;
}
