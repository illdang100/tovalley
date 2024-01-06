package kr.ac.kumoh.illdang100.tovalley.web.page;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.Accident;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalRegion;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalRegionRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalWeatherRedisRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather.*;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Sql("classpath:db/teardown.sql")
class MainPageApiControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private NationalRegionRepository nationalRegionRepository;
    @Autowired
    private NationalWeatherRedisRepository nationalWeatherRedisRepository;
    @Autowired
    private SpecialWeatherRedisRepository specialWeatherRedisRepository;
    @Autowired
    private WaterPlaceRepository waterPlaceRepository;
    @Autowired
    private AccidentRepository accidentRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        dataSetting();
    }

    @Test
    public void getMainPage_테스트() throws Exception {

        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/main-page"));

        // then

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nationalWeather.size()").value(5))
                .andExpect(jsonPath("$.data.weatherAlert.weatherAlerts.size()").value(2))
                .andExpect(jsonPath("$.data.weatherAlert.weatherPreAlerts.size()").value(1))
                .andExpect(jsonPath("$.data.accidentCountDto.province").value("전국"))
                .andExpect(jsonPath("$.data.accidentCountDto.totalDeathCnt").value(6))
                .andExpect(jsonPath("$.data.accidentCountDto.totalDisappearanceCnt").value(0))
                .andExpect(jsonPath("$.data.accidentCountDto.totalInjuryCnt").value(10))
                .andExpect(jsonPath("$.data.nationalPopularWaterPlaces[0].waterPlaceName").value("울릉계곡"))
                .andDo(MockMvcResultHandlers.print());
    }

    private void dataSetting() {

        WaterPlace waterPlace1 = waterPlaceRepository.save(newWaterPlace("서울계곡", "서울특별시", 4.9, 2010));
        WaterPlace waterPlace2 = waterPlaceRepository.save(newWaterPlace("금오계곡", "경상북도", 2.0, 10));
        WaterPlace waterPlace3 = waterPlaceRepository.save(newWaterPlace("대전계곡", "대전광역시", 2.5, 130));
        WaterPlace waterPlace4 = waterPlaceRepository.save(newWaterPlace("부산계곡", "부산광역시", 4.0, 410));
        WaterPlace waterPlace5 = waterPlaceRepository.save(newWaterPlace("울산계곡", "울산광역시", 3.8, 110));
        WaterPlace waterPlace6 = waterPlaceRepository.save(newWaterPlace("대구계곡", "대구광역시", 1.1, 110));
        WaterPlace waterPlace7 = waterPlaceRepository.save(newWaterPlace("울릉계곡", "경상북도", 5.0, 3));
        WaterPlace waterPlace8 = waterPlaceRepository.save(newWaterPlace("제주계곡", "제주특별자치도", 4.1, 1100));

        NationalRegion seoul = nationalRegionRepository.save(newNationalRegion("서울"));
        NationalRegion jeju = nationalRegionRepository.save(newNationalRegion("제주"));
        NationalRegion ulleng = nationalRegionRepository.save(newNationalRegion("울릉"));
        NationalRegion daegu = nationalRegionRepository.save(newNationalRegion("대구"));
        NationalRegion busan = nationalRegionRepository.save(newNationalRegion("부산"));

        nationalWeatherRedisRepository.save(newNationalWeather(seoul, "Rain", LocalDate.now()));
        nationalWeatherRedisRepository.save(newNationalWeather(seoul, "Rain", LocalDate.now().plusDays(1)));
        nationalWeatherRedisRepository.save(newNationalWeather(seoul, "Rain", LocalDate.now().plusDays(2)));
        nationalWeatherRedisRepository.save(newNationalWeather(seoul, "Clouds", LocalDate.now().plusDays(3)));
        nationalWeatherRedisRepository.save(newNationalWeather(seoul, "Snow", LocalDate.now().plusDays(4)));

        nationalWeatherRedisRepository.save(newNationalWeather(jeju, "Clear", LocalDate.now()));
        nationalWeatherRedisRepository.save(newNationalWeather(jeju, "Rain", LocalDate.now().plusDays(1)));
        nationalWeatherRedisRepository.save(newNationalWeather(jeju, "Rain", LocalDate.now().plusDays(2)));
        nationalWeatherRedisRepository.save(newNationalWeather(jeju, "Thunderstorm", LocalDate.now().plusDays(3)));
        nationalWeatherRedisRepository.save(newNationalWeather(jeju, "Mist", LocalDate.now().plusDays(4)));

        nationalWeatherRedisRepository.save(newNationalWeather(ulleng, "Clear", LocalDate.now()));
        nationalWeatherRedisRepository.save(newNationalWeather(ulleng, "Clear", LocalDate.now().plusDays(1)));
        nationalWeatherRedisRepository.save(newNationalWeather(ulleng, "Mist", LocalDate.now().plusDays(2)));
        nationalWeatherRedisRepository.save(newNationalWeather(ulleng, "Mist", LocalDate.now().plusDays(3)));
        nationalWeatherRedisRepository.save(newNationalWeather(ulleng, "Mist", LocalDate.now().plusDays(4)));

        nationalWeatherRedisRepository.save(newNationalWeather(daegu, "Thunderstorm", LocalDate.now()));
        nationalWeatherRedisRepository.save(newNationalWeather(daegu, "Thunderstorm", LocalDate.now().plusDays(1)));
        nationalWeatherRedisRepository.save(newNationalWeather(daegu, "Rain", LocalDate.now().plusDays(2)));
        nationalWeatherRedisRepository.save(newNationalWeather(daegu, "Clear", LocalDate.now().plusDays(3)));
        nationalWeatherRedisRepository.save(newNationalWeather(daegu, "Rain", LocalDate.now().plusDays(4)));

        nationalWeatherRedisRepository.save(newNationalWeather(busan, "Clear", LocalDate.now()));
        nationalWeatherRedisRepository.save(newNationalWeather(busan, "Clear", LocalDate.now().plusDays(1)));
        nationalWeatherRedisRepository.save(newNationalWeather(busan, "Clouds", LocalDate.now().plusDays(2)));
        nationalWeatherRedisRepository.save(newNationalWeather(busan, "Clouds", LocalDate.now().plusDays(3)));
        nationalWeatherRedisRepository.save(newNationalWeather(busan, "Clouds", LocalDate.now().plusDays(4)));

        specialWeatherRedisRepository.save(
                newSpecialWeather(LocalDateTime.now().minusHours(1), WeatherAlertType.WINDSTORM,
                        SpecialWeatherEnum.BREAKING, "강풍주의보",
                        Arrays.asList(SpecialWeatherDetail.builder()
                                .content("울릉도.독도")
                                .build())));
        specialWeatherRedisRepository.save(
                newSpecialWeather(LocalDateTime.now().minusHours(2), WeatherAlertType.ROUGH_SEA,
                        SpecialWeatherEnum.BREAKING, "풍랑주의보",
                        Arrays.asList(SpecialWeatherDetail.builder()
                                .content("남해동부먼바다, 동해중부먼바다, 동해남부먼바다")
                                .build())));
        specialWeatherRedisRepository.save(
                newSpecialWeather(LocalDateTime.now().minusHours(2), WeatherAlertType.ROUGH_SEA,
                        SpecialWeatherEnum.PRELIMINARY, "풍랑 예비특보",
                        Arrays.asList(SpecialWeatherDetail.builder()
                                .content("06월 07일 아침 : 동해중부앞바다, 동해남부앞바다(경북북부앞바다, 경북남부앞바다)")
                                .build())));

        Accident accident1 =
                accidentRepository.save(newAccident(waterPlace1, LocalDate.now(), AccidentEnum.INJURY, 10));
        Accident accident2 =
                accidentRepository.save(newAccident(waterPlace1, LocalDate.now(), AccidentEnum.DEATH, 6));

        em.clear();
    }
}