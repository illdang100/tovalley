package kr.ac.kumoh.illdang100.tovalley.domain.accident;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AccidentEnum {

    DEATH("사망"), DISAPPEARANCE("실종"), INJURY("부상"), ETC("기타");
    private String value;
}
