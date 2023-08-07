package kr.ac.kumoh.illdang100.tovalley.domain.waterplace;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WaterPlaceCategory {

    VALLEY("계곡"), RIVER("하천");
    private String value;
}
