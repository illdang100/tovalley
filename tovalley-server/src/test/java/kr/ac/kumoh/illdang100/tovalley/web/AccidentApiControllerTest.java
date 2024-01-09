package kr.ac.kumoh.illdang100.tovalley.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.Accident;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalRegionRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather.*;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Sql("classpath:db/teardown.sql")
class AccidentApiControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private NationalRegionRepository nationalRegionRepository;
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
    public void getAccidentsByProvince_테스트() throws Exception {

        // given

        // when
        ResultActions resultActions1 = mvc.perform(get("/api/main-page/accidents")
                .param("province", "SEOUL")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));

        ResultActions resultActions2 = mvc.perform(get("/api/main-page/accidents")
                .param("province", "NATIONWIDE")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));

        // then

        int month = LocalDate.now().getMonthValue();

        resultActions1
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.province").value("서울"))
                .andExpect(jsonPath("$.data.totalDeathCnt").value(6))
                .andExpect(jsonPath("$.data.totalDisappearanceCnt").value(0))
                .andExpect(jsonPath("$.data.totalInjuryCnt").value(10))
                .andExpect(jsonPath("$.data.accidentCountPerMonth[" + (month - 1) + "].deathCnt").value(6))
                .andExpect(jsonPath("$.data.accidentCountPerMonth[" + (month - 1) + "].injuryCnt").value(10))
                .andDo(MockMvcResultHandlers.print());

        resultActions2
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.province").value("전국"))
                .andExpect(jsonPath("$.data.totalDeathCnt").value(12))
                .andExpect(jsonPath("$.data.totalDisappearanceCnt").value(6))
                .andExpect(jsonPath("$.data.totalInjuryCnt").value(46))
                .andExpect(jsonPath("$.data.accidentCountPerMonth[" + (month - 1) + "].deathCnt").value(12))
                .andExpect(jsonPath("$.data.accidentCountPerMonth[" + (month - 1) + "].injuryCnt").value(46))
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

        List<Accident> accidentList = new ArrayList<>();
        LocalDate now = LocalDate.now();
        accidentList.add(newAccident(waterPlace1, now, AccidentEnum.INJURY, 10));
        accidentList.add(newAccident(waterPlace1, now, AccidentEnum.DEATH, 6));
        accidentList.add(newAccident(waterPlace2, now, AccidentEnum.DEATH, 6));
        accidentList.add(newAccident(waterPlace3, now, AccidentEnum.DISAPPEARANCE, 6));
        accidentList.add(newAccident(waterPlace4, now, AccidentEnum.INJURY, 6));
        accidentList.add(newAccident(waterPlace4, now, AccidentEnum.INJURY, 6));
        accidentList.add(newAccident(waterPlace5, now, AccidentEnum.INJURY, 6));
        accidentList.add(newAccident(waterPlace5, now, AccidentEnum.INJURY, 6));
        accidentList.add(newAccident(waterPlace5, now, AccidentEnum.INJURY, 6));
        accidentList.add(newAccident(waterPlace5, now, AccidentEnum.INJURY, 6));
        accidentRepository.saveAll(accidentList);

        em.clear();
    }
}