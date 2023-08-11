package kr.ac.kumoh.illdang100.tovalley.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProvinceEnum {

    ULSAN("울산"), DAEJEON("대전"), GWANGJU("광주"), BUSAN("부산"),
    SEOUL("서울"), JEJU("제주"), SEJONG("세종"),
    JEOLLA("전라"), GYEONGGI("경기"), GYEONGSANG("경상"), CHUNGCHEONG("충청"), GANGWON("강원"),
    NATIONWIDE("전국");
    private String value;
}
