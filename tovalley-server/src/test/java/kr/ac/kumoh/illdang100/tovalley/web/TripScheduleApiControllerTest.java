package kr.ac.kumoh.illdang100.tovalley.web;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripScheduleRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.*;
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
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Sql("classpath:db/teardown.sql")
class TripScheduleApiControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private WaterPlaceRepository waterPlaceRepository;
    @Autowired
    private TripScheduleRepository tripScheduleRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        dataSetting();
    }

    @Test
    public void getTripAttendeesByWaterPlace_success_test() throws Exception {

        // given
        Long waterPlaceId = 1L;
        YearMonth now = YearMonth.now();

        // when
        ResultActions resultActions =
                mvc.perform(get("/api/auth/water-places/" + waterPlaceId + "/trip-schedules?yearMonth=" + now));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getTripAttendeesByWaterPlace_failure_test() throws Exception {

        // given
        Long waterPlaceId = 4000L;
        YearMonth now = YearMonth.now();

        // when
        ResultActions resultActions =
                mvc.perform(get("/api/auth/water-places/" + waterPlaceId + "/trip-schedules?yearMonth=" + now));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    private void dataSetting() {

        List<Member> memberList = new ArrayList<>();
        Member member1 = newMember("kakao_1234", "member1");
        memberList.add(member1);
        memberRepository.saveAll(memberList);

        List<WaterPlace> waterPlaceList = new ArrayList<>();
        WaterPlace waterPlace = newWaterPlace("서울계곡", "서울특별시", 0.0, 0);
        waterPlaceList.add(waterPlace);
        waterPlaceRepository.saveAll(waterPlaceList);

        List<TripSchedule> tripScheduleList = new ArrayList<>();
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.now().minusDays(1), 50));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.now().minusDays(3), 150));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.now().minusDays(5), 60));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.now().minusDays(7), 34));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.now().minusDays(9), 11));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.now().minusDays(11), 94));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.now().minusDays(15), 11));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.now().plusDays(1), 51));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.now().plusDays(3), 111));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.now().plusDays(5), 61));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.now().plusDays(7), 22));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.now().plusDays(9), 26));
        tripScheduleList.add(newTripSchedule(member1, waterPlace, LocalDate.now().plusDays(11), 98));
        tripScheduleRepository.saveAll(tripScheduleList);

        em.clear();
    }
}