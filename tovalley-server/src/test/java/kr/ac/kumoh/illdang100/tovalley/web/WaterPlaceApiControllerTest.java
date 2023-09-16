package kr.ac.kumoh.illdang100.tovalley.web;

import kr.ac.kumoh.illdang100.tovalley.domain.CityEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Sql("classpath:db/teardown.sql")
public class WaterPlaceApiControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private WaterPlaceRepository waterPlaceRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        dataSetting();
    }

    @Test
    public void getWaterPlaces_success_test() throws Exception {
        // given

        // when
        ResultActions resultActions =
                mvc.perform(get("/api/water-place/list")
                        .param("province", "경상북도")
                        .param("searchWord", "계곡")
                        .param("city", CityEnum.문경시.toString())
                        .param("sortCond", "rating")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void getPopularWaterPlaces_테스트() throws Exception {

        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/main-page/popular-water-places")
                .param("cond", "REVIEW")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].waterPlaceName").value("서울계곡"))
                .andExpect(jsonPath("$.data[4].waterPlaceName").value("세종계곡"))
                .andDo(MockMvcResultHandlers.print());
    }

    private void dataSetting() {

        List<Member> memberList = new ArrayList<>();
        Member member1 = newMember("kakao_1234", "member1");
        memberList.add(member1);
        memberRepository.saveAll(memberList);

        List<WaterPlace> waterPlaceList = new ArrayList<>();
        WaterPlace waterPlace1 = newWaterPlaceWithCity("서울계곡", "서울특별시", CityEnum.관악구.toString(), 4.0, 10);
        WaterPlace waterPlace2 = newWaterPlaceWithCity("부산계곡", "부산광역시", CityEnum.해운대구.toString(), 4.1, 9);
        WaterPlace waterPlace3 = newWaterPlaceWithCity("문경계곡", "경상북도", CityEnum.문경시.toString(),4.2, 8);
        WaterPlace waterPlace4 = newWaterPlaceWithCity("안동계곡", "경상북도", CityEnum.안동시.toString(),4.3, 7);
        WaterPlace waterPlace5 = newWaterPlaceWithCity("세종계곡", "세종특별자치시", CityEnum.세종시.toString(),4.4, 6);
        waterPlaceList.add(waterPlace1);
        waterPlaceList.add(waterPlace2);
        waterPlaceList.add(waterPlace3);
        waterPlaceList.add(waterPlace4);
        waterPlaceList.add(waterPlace5);
        waterPlaceRepository.saveAll(waterPlaceList);

        em.clear();
    }
}
