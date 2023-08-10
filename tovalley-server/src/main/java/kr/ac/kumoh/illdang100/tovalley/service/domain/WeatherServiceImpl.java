package kr.ac.kumoh.illdang100.tovalley.service.domain;

import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalRegion;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalRegionRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalWeather;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalWeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.MainPageRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.weather.WeatherRespDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeatherServiceImpl implements WeatherService {

    private final NationalWeatherRepository nationalWeatherRepository;

    @Override
    public List<NationalWeatherRespDto> getNationalWeathers() {
        List<NationalWeather> nationalWeatherWithNationalRegion = nationalWeatherRepository.findAllWithNationalRegion();

        Map<LocalDate, List<DailyNationalWeatherDto>> weatherByDate = groupWeatherDataByDate(nationalWeatherWithNationalRegion);

        return createNationalWeatherRespDtoList(weatherByDate);
    }

    private Map<LocalDate, List<DailyNationalWeatherDto>> groupWeatherDataByDate(List<NationalWeather> nationalWeathers) {
        Map<LocalDate, List<DailyNationalWeatherDto>> weatherByDate = new HashMap<>();

        for (NationalWeather nationalWeather : nationalWeathers) {
            LocalDate weatherDate = nationalWeather.getWeatherDate();
            DailyNationalWeatherDto dailyNationalWeatherDto = createDailyNationalWeatherDto(nationalWeather);

            weatherByDate.computeIfAbsent(weatherDate, key -> new ArrayList<>()).add(dailyNationalWeatherDto);
        }

        return weatherByDate;
    }

    private DailyNationalWeatherDto createDailyNationalWeatherDto(NationalWeather nationalWeather) {
        return DailyNationalWeatherDto.builder()
                .region(nationalWeather.getNationalRegion().getRegionName())
                .weatherIcon(nationalWeather.getClimateIcon())
                .weatherDesc(nationalWeather.getClimateDescription())
                .minTemp(nationalWeather.getLowestTemperature())
                .maxTemp(nationalWeather.getHighestTemperature())
                .rainPrecipitation(nationalWeather.getRainPrecipitation())
                .build();
    }

    private List<NationalWeatherRespDto> createNationalWeatherRespDtoList(Map<LocalDate, List<DailyNationalWeatherDto>> weatherByDate) {
        List<NationalWeatherRespDto> result = new ArrayList<>();

        for (Map.Entry<LocalDate, List<DailyNationalWeatherDto>> entry : weatherByDate.entrySet()) {
            NationalWeatherRespDto respDto = new NationalWeatherRespDto(entry.getKey(), entry.getValue());
            result.add(respDto);
        }

        return result;
    }

    @Override
    public List<SpecialWeatherRespDto> getAllSpecialWeatherAdvisories() {
        return null;
    }

    @Override
    public WaterPlaceWeatherRespDto getValleyWeatherData(Long waterPlaceId) {

        // 조회한 계곡 날씨의 lastModifiedDate 시간을 확인해 현재시간으로부터 3시간 이전이라면 전부 삭제하고 새로 가져오기!!
        // 그리고 현재 날짜 이전 날짜 정보가 있다면 삭제하고 새로 가져오기!!

        return null;
    }
}
