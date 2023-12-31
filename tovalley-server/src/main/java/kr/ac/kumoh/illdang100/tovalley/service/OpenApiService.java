package kr.ac.kumoh.illdang100.tovalley.service;

import kr.ac.kumoh.illdang100.tovalley.domain.Coordinate;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.water_place_weather.WaterPlaceWeather;

import java.util.List;

public interface OpenApiService {

    /**
     * 전국 몇몇 지역의 날씨 정보를 Open API로부터 가져와 데이터베이스에 저장
     * 백령, 서울, 춘천, 강릉, 수원, 청주, 울릉/독도, 대전, 안동, 전주, 대구, 울산, 광주, 목포, 부산, 제주, 여수
     * 이 메서드는 일정 시간마다 주기적으로 실행된다.
     */
    void fetchAndSaveNationalWeatherData();

    /**
     * 전국 특보 정보를 Open API로부터 가져와 데이터베이스에 저장
     * 이 메서드는 일정 시간마다 주기적으로 실행된다.
     */
    void fetchAndSaveSpecialWeatherData();

    /**
     * 특정 물놀이 장소 날씨를 Open API로부터 가져와 데이터베이스에 저장
     */
    List<WaterPlaceWeather> fetchAndSaveWaterPlaceWeatherData(Long waterPlaceId);

    /**
     * 전국 물놀이 지역 정보를 Open API로부터 가져와 데이터베이스에 저장
     */
    void fetchAndSaveWaterPlacesData();

    /**
     * @param completeAddress 클라이언트로부터 받아온 물놀이 장소 주소 정보
     * @return 해당 물놀이 장소 좌표(위도, 경도)
     */
    Coordinate getGeoDataByAddress(String completeAddress);
}
