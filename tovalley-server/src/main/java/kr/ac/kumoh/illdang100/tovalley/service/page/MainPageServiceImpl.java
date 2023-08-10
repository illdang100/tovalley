package kr.ac.kumoh.illdang100.tovalley.service.page;

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

    @Override
    public MainPageAllRespDto getMainPageAllData() {

        List<NationalWeatherRespDto> nationalWeathers = weatherService.getNationalWeathers();

        return null;
    }

    @Override
    public AccidentCountDto getTotalAccidents(RetrieveYearlyAccidentCondition yearlyAccidentCondition) {
        return null;
    }

    @Override
    public List<NationalPopularWaterPlacesDto> getPopularWaterPlaces(RetrievePopularWaterPlacesCondition popularWaterPlacesCondition) {
        return null;
    }
}
