package kr.ac.kumoh.illdang100.tovalley.service.page;

import kr.ac.kumoh.illdang100.tovalley.domain.ProvinceEnum;
import kr.ac.kumoh.illdang100.tovalley.service.domain.AccidentService;
import kr.ac.kumoh.illdang100.tovalley.service.domain.WaterPlaceService;
import kr.ac.kumoh.illdang100.tovalley.service.domain.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.MainPageReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.page.MainPageRespDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainPageServiceImpl implements MainPageService {

    private final WeatherService weatherService;
    private final AccidentService accidentService;
    private final WaterPlaceService waterPlaceService;

    /**
     * @methodnme: getMainPageAllData
     * @author: JYeonJun
     * @description: 메인페이지 정보 요청시 데이터를 취합해서 보내주는 메서드
     *
     * @return: 전국 날씨 정보, 특보, 예비 특보, 지역별 사건 사고 발생률, 인기 계곡 현황
     */
    @Override
    public MainPageAllRespDto getMainPageAllData() {

        List<NationalWeatherRespDto> nationalWeatherDto = weatherService.getNationalWeathers();
        AlertRespDto alertRespDto = weatherService.getAllSpecialWeathers();
        AccidentCountDto nationalAccidentCountDto = accidentService.getAccidentCntPerMonthByProvince(ProvinceEnum.NATIONWIDE.getValue());
        List<NationalPopularWaterPlacesDto> popularWaterPlaces = waterPlaceService.getPopularWaterPlaces("RATING");

        return new MainPageAllRespDto(nationalWeatherDto, alertRespDto, nationalAccidentCountDto, popularWaterPlaces);
    }
}
