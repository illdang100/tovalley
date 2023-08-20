package kr.ac.kumoh.illdang100.tovalley.service.domain.accident;

import kr.ac.kumoh.illdang100.tovalley.domain.accident.Accident;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import kr.ac.kumoh.illdang100.tovalley.service.accident.AccidentServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentRespDto.*;
import static org.mockito.Mockito.when;

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

        List<Accident> accidents1 = new ArrayList<>();
        List<Accident> accidents2 = new ArrayList<>();
        WaterPlace waterPlace1 =
                newMockWaterPlace(1L, "금오계곡", "경상북도", 3.0, 10);
        WaterPlace waterPlace2 =
                newMockWaterPlace(2L, "서울계곡", "서울특별시", 3.0, 10);
        accidents1.add(newMockAccident(1L, waterPlace1, LocalDate.now().minusMonths(1), AccidentEnum.INJURY, 3));
        accidents1.add(newMockAccident(2L, waterPlace1, LocalDate.now().minusMonths(13), AccidentEnum.DEATH, 1));
        accidents1.add(newMockAccident(3L, waterPlace1, LocalDate.now().minusMonths(25), AccidentEnum.DISAPPEARANCE, 5));

        accidents2.add(newMockAccident(1L, waterPlace1, LocalDate.now().minusMonths(1), AccidentEnum.INJURY, 3));
        accidents2.add(newMockAccident(2L, waterPlace1, LocalDate.now().minusMonths(13), AccidentEnum.DEATH, 1));
        accidents2.add(newMockAccident(3L, waterPlace1, LocalDate.now().minusMonths(25), AccidentEnum.DISAPPEARANCE, 5));

        accidents1.add(newMockAccident(4L, waterPlace2, LocalDate.now().minusMonths(1), AccidentEnum.INJURY, 3));
        accidents1.add(newMockAccident(5L, waterPlace2, LocalDate.now().minusMonths(13), AccidentEnum.DEATH, 1));
        accidents1.add(newMockAccident(6L, waterPlace2, LocalDate.now().minusMonths(25), AccidentEnum.DISAPPEARANCE, 5));

        // stub1
        when(accidentRepository.findAll()).thenReturn(accidents1);

        // stub2
        when(accidentRepository.findByProvinceStartingWithProvince(province2)).thenReturn(accidents2);

        // when
        AccidentCountDto result1 = accidentService.getAccidentCntPerMonthByProvince(province1);
        AccidentCountDto result2 = accidentService.getAccidentCntPerMonthByProvince(province2);

        // then
        Assertions.assertThat(result1.getProvince()).isEqualTo("전국");
        Assertions.assertThat(result1.getTotalDeathCnt()).isEqualTo(2);
        Assertions.assertThat(result1.getTotalInjuryCnt()).isEqualTo(6);
        Assertions.assertThat(result1.getTotalDisappearanceCnt()).isEqualTo(10);

        Assertions.assertThat(result2.getProvince()).isEqualTo("경상");
        Assertions.assertThat(result2.getTotalDeathCnt()).isEqualTo(1);
        Assertions.assertThat(result2.getTotalInjuryCnt()).isEqualTo(3);
        Assertions.assertThat(result2.getTotalDisappearanceCnt()).isEqualTo(5);
    }
}