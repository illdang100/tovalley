package kr.ac.kumoh.illdang100.tovalley.dummy;

import kr.ac.kumoh.illdang100.tovalley.domain.Coordinate;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.Accident;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalRegion;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalWeather;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather.SpecialWeather;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather.SpecialWeatherDetail;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather.SpecialWeatherEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather.WeatherAlertType;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.water_place_weather.WaterPlaceWeather;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    protected Accident newAccident(WaterPlace waterPlace, LocalDate accidentDate,
                                       AccidentEnum accidentCondition, Integer peopleNum) {

        return Accident.builder()
                .waterPlace(waterPlace)
                .accidentDate(accidentDate)
                .accidentCondition(accidentCondition)
                .peopleNum(peopleNum)
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

    protected NationalRegion newNationalRegion(String regionName) {

        return NationalRegion.builder()
                .regionName(regionName)
                .coordinate(new Coordinate("11.11111111", "111.11111111"))
                .build();
    }

    protected NationalRegion newMockNationalRegion(Long id, String regionName) {

        return NationalRegion.builder()
                .id(id)
                .regionName(regionName)
                .coordinate(new Coordinate("11.11111111", "111.11111111"))
                .build();
    }

    protected NationalWeather newNationalWeather(NationalRegion nationalRegion, String climate,
                                                     LocalDate weatherDate) {

        return NationalWeather.builder()
                .nationalRegion(nationalRegion)
                .climate(climate)
                .climateIcon("10d")
                .climateDescription("굵은 비")
                .lowestTemperature(24.7)
                .highestTemperature(35.9)
                .weatherDate(weatherDate)
                .rainPrecipitation(0.78)
                .build();
    }

    protected NationalWeather newMockNationalWeather(Long id, NationalRegion nationalRegion, String climate,
                                                     LocalDate weatherDate) {

        return NationalWeather.builder()
                .id(id)
                .nationalRegion(nationalRegion)
                .climate(climate)
                .climateIcon("10d")
                .climateDescription("굵은 비")
                .lowestTemperature(24.7)
                .highestTemperature(35.9)
                .weatherDate(weatherDate)
                .rainPrecipitation(0.78)
                .build();
    }

    protected SpecialWeather newSpecialWeather (LocalDateTime time, WeatherAlertType weatherAlertType,
                                                SpecialWeatherEnum category, String title) {

        return SpecialWeather.builder()
                .announcementTime(time)
                .effectiveTime(time)
                .weatherAlertType(weatherAlertType)
                .category(category)
                .title(title)
                .build();
    }

    protected SpecialWeatherDetail newSpecialWeatherDetail (SpecialWeather specialWeather, String content) {

        return SpecialWeatherDetail.builder()
                .specialWeather(specialWeather)
                .content(content)
                .build();

    }
}
