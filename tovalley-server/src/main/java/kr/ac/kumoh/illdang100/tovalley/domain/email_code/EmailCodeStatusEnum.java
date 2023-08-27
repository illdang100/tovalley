package kr.ac.kumoh.illdang100.tovalley.domain.email_code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EmailCodeStatusEnum {
    COMPLETED("완료됨"), PENDING("보류중");
    private String value;
}
