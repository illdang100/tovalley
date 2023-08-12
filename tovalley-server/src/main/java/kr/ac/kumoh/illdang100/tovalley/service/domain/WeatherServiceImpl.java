package kr.ac.kumoh.illdang100.tovalley.service.domain;

import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalWeather;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalWeatherRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather.SpecialWeather;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather.SpecialWeatherDetail;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather.SpecialWeatherDetailRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather.SpecialWeatherEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.MainPageRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.weather.WeatherRespDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeatherServiceImpl implements WeatherService {

    private final NationalWeatherRepository nationalWeatherRepository;
    private final SpecialWeatherDetailRepository specialWeatherDetailRepository;

    /**
     * @methodnme: getNationalWeathers
     * @author: JYeonJun
     * @description: 전국 특정 지역 날씨 정보 조회
     *
     * @return: Openweather 날씨 정보
     */
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

    /**
     * @methodnme: getAllSpecialWeathers
     * @author: JYeonJun
     * @description: 전국 특보 정보 조회
     *
     * @return: 기상청 전국 특보 정보
     */
    @Override
    public AlertRespDto getAllSpecialWeathers() {
        List<SpecialWeatherDetail> specialWeatherDetailsWithSpecialWeather =
                specialWeatherDetailRepository.findAllWithSpecialWeather();


        Map<SpecialWeather, List<SpecialWeatherDetail>> weatherDetailsMap =
                groupWeatherDetails(specialWeatherDetailsWithSpecialWeather);

        List<WeatherAlertDto> weatherAlerts = new ArrayList<>();
        List<WeatherPreliminaryAlertDto> weatherPreAlerts = new ArrayList<>();

        weatherDetailsMap.forEach((specialWeather, details) -> {
            String weatherAlertType = specialWeather.getWeatherAlertType().getValue();
            String title = specialWeather.getTitle();
            LocalDateTime announcementTime = specialWeather.getAnnouncementTime();
            LocalDateTime effectiveTime = specialWeather.getEffectiveTime();

            if (specialWeather.getCategory() == SpecialWeatherEnum.BREAKING) {
                String content = details.get(0).getContent();
                weatherAlerts.add(new WeatherAlertDto(weatherAlertType, title, announcementTime, effectiveTime, content));
            } else if (specialWeather.getCategory() == SpecialWeatherEnum.PRELIMINARY) {
                List<WeatherPreliminaryAlertContentDto> contentDtos = createContentDtos(details);

                weatherPreAlerts.add(new WeatherPreliminaryAlertDto(announcementTime, title, weatherAlertType, contentDtos));
            }
        });

        return new AlertRespDto(weatherAlerts, weatherPreAlerts);
    }

    private Map<SpecialWeather, List<SpecialWeatherDetail>> groupWeatherDetails(List<SpecialWeatherDetail> details) {
        return details.stream()
                .collect(Collectors.groupingBy(SpecialWeatherDetail::getSpecialWeather));
    }

    private List<WeatherPreliminaryAlertContentDto> createContentDtos(List<SpecialWeatherDetail> details) {
        return details.stream()
                .map(d -> new WeatherPreliminaryAlertContentDto(d.getContent()))
                .collect(Collectors.toList());
    }

    @Override
    public WaterPlaceWeatherRespDto getValleyWeatherData(Long waterPlaceId) {

        // 조회한 계곡 날씨의 lastModifiedDate 시간을 확인해 현재시간으로부터 3시간 이전이라면 전부 삭제하고 새로 가져오기!!
        // 그리고 현재 날짜 이전 날짜 정보가 있다면 삭제하고 새로 가져오기!!

        return null;
    }
}
