package kr.ac.kumoh.illdang100.tovalley.dummy;

import kr.ac.kumoh.illdang100.tovalley.domain.Coordinate;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.Accident;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;

import java.time.LocalDate;

public class DummyObject {

    protected WaterPlace newWaterPlace(String name, String province, Double rating, Integer reviewCnt) {

        return WaterPlace.builder()
                .waterPlaceName(name)
                .province(province)
                .city("city")
                .town("town")
                .subLocation("subLocation")
                .address("address")
                .waterPlaceCategory("계곡")
                .coordinate(new Coordinate("38.10000000", "128.10000000"))
                .managementType("일반지역")
                .rating(rating)
                .reviewCount(reviewCnt)
                .build();
    }

    protected WaterPlace newMockWaterPlace(Long id, String name, String province, Double rating, Integer reviewCnt) {

        return WaterPlace.builder()
                .id(id)
                .waterPlaceName(name)
                .province(province)
                .city("city")
                .town("town")
                .subLocation("subLocation")
                .address("address")
                .waterPlaceCategory("계곡")
                .coordinate(new Coordinate("38.10000000", "128.10000000"))
                .managementType("일반지역")
                .rating(rating)
                .reviewCount(reviewCnt)
                .build();
    }

    protected Accident newMockAccident(Long id, WaterPlace waterPlace, LocalDate accidentDate,
                                       AccidentEnum accidentCondition, Integer peopleNum) {

        return Accident.builder()
                .id(id)
                .waterPlace(waterPlace)
                .accidentDate(accidentDate)
                .accidentCondition(accidentCondition)
                .peopleNum(peopleNum)
                .build();
    }
}
