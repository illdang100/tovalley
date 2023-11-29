package kr.ac.kumoh.illdang100.tovalley.service.accident;

import kr.ac.kumoh.illdang100.tovalley.domain.accident.Accident;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentRespDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AccidentServiceImplTest extends DummyObject {

    @InjectMocks
    private AccidentServiceImpl accidentService;

    @Mock
    private AccidentRepository accidentRepository;

    @Test
    public void getAccidentCntPerMonthByProvince_테스트() {

        // given
        String province1 = "전국";
        String province2 = "경상";

        LocalDate now = LocalDate.now();
        int year = now.getYear();

        List<Accident> accidents1 = new ArrayList<>();
        List<Accident> accidents2 = new ArrayList<>();
        WaterPlace waterPlace1 =
                newMockWaterPlace(1L, "금오계곡", "경상북도", 3.0, 10);
        WaterPlace waterPlace2 =
                newMockWaterPlace(2L, "서울계곡", "서울특별시", 3.0, 10);

        accidents2.add(newMockAccident(4L, waterPlace1, now, AccidentEnum.INJURY, 3));
        accidents2.add(newMockAccident(5L, waterPlace1, now, AccidentEnum.DEATH, 1));
        accidents2.add(newMockAccident(6L, waterPlace1, now, AccidentEnum.DISAPPEARANCE, 5));


        // stub1
        when(accidentRepository.findByYear(year)).thenReturn(accidents1);

        // when
        AccidentCountDto result1 = accidentService.getAccidentCntPerMonthByProvince(province1);

        // stub2
        when(accidentRepository.findByProvinceStartingWithProvinceAndYear(province2, year)).thenReturn(accidents2);

        AccidentCountDto result2 = accidentService.getAccidentCntPerMonthByProvince(province2);

        // then
        assertThat(result1.getProvince()).isEqualTo("전국");
        assertThat(result1.getTotalDeathCnt()).isEqualTo(0);
        assertThat(result1.getTotalInjuryCnt()).isEqualTo(0);
        assertThat(result1.getTotalDisappearanceCnt()).isEqualTo(0);

        assertThat(result2.getProvince()).isEqualTo("경상");
        assertThat(result2.getTotalDeathCnt()).isEqualTo(1);
        assertThat(result2.getTotalInjuryCnt()).isEqualTo(3);
        assertThat(result2.getTotalDisappearanceCnt()).isEqualTo(5);
    }
}