package kr.ac.kumoh.illdang100.tovalley.service.domain;

import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.weather.WeatherRespDto.*;

public interface WeatherService {

    /**
     * 전국 몇몇 지역의 현재 날씨 정보를 조회
     * 백령, 서울, 춘천, 강릉, 수원, 청주, 울릉/독도, 대전, 안동, 전주, 대구, 울산, 광주, 목포, 부산, 제주, 여수
     * @return 현재 날씨 정보
     */
    List<NationalWeatherRespDto> getNationalWeathers();

    /**
     * 전국 특보 정보 조회
     * @return 전국 특보 정보 목록
     */
    List<SpecialWeatherRespDto> getAllSpecialWeatherAdvisories();

    /**
     * 특정 계곡의 날씨 정보를 가져옵니다.
     *
     * @param waterPlaceId 특정 계곡의 ID
     * @return 특정 계곡의 날씨 정보
     */
    WaterPlaceWeatherRespDto getValleyWeatherData(Long waterPlaceId);
}