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

    @Override
    public MainPageAllRespDto getMainPageAllData() {

        List<NationalWeatherRespDto> nationalWeatherDto = weatherService.getNationalWeathers();
        AlertRespDto alertRespDto = weatherService.getAllSpecialWeathers();
        AccidentCountDto nationalAccidentCountDto = accidentService.getAccidentCntPerMonthByProvince(ProvinceEnum.NATIONWIDE.getValue());
        List<NationalPopularWaterPlacesDto> popularWaterPlaces = waterPlaceService.getPopularWaterPlaces("RATING");

        return new MainPageAllRespDto(nationalWeatherDto, alertRespDto, nationalAccidentCountDto, popularWaterPlaces);
    }

    @Override
    public AccidentCountDto getTotalAccidents(String province) {

        return accidentService.getAccidentCntPerMonthByProvince(province);
    }

    @Override
    public List<NationalPopularWaterPlacesDto> getPopularWaterPlaces(String cond) {
        return waterPlaceService.getPopularWaterPlaces(cond);
    }
}
