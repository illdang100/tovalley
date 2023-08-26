package kr.ac.kumoh.illdang100.tovalley.domain.accident;

import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentRespDto.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class AccidentRepositoryImplTest extends DummyObject {

    @Autowired
    private AccidentRepository accidentRepository;
    @Autowired
    private WaterPlaceRepository waterPlaceRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        autoIncrementReset();
        dataSetting();
        em.flush();
        em.clear();
    }

    @Test
    public void findAccidentDetailRespDtoByWaterPlaceId_전체조회_test() {

        // given
        Long waterPlaceId = 1L;
        RetrieveAccidentCondition retrieveAccidentCondition = new RetrieveAccidentCondition(null, null);
        PageRequest pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "accidentDate"));

        // when
        List<AccidentDetailRespDto> content = accidentRepository.findAccidentDetailRespDtoByWaterPlaceId(waterPlaceId, retrieveAccidentCondition, pageable)
                .getContent();

        // then

        LocalDate now = LocalDate.now();
        assertThat(content.size()).isEqualTo(5);
        assertThat(content.get(0).getAccidentDate()).isEqualTo(now.minusMonths(1));
        assertThat(content.get(0).getAccidentCondition()).isEqualTo(AccidentEnum.INJURY.getValue());
        assertThat(content.get(0).getPeopleNum()).isEqualTo(12);
        assertThat(content.get(1).getAccidentDate()).isEqualTo(now.minusMonths(2));
        assertThat(content.get(1).getAccidentCondition()).isEqualTo(AccidentEnum.DISAPPEARANCE.getValue());
        assertThat(content.get(2).getAccidentDate()).isEqualTo(now.minusMonths(3));
        assertThat(content.get(3).getAccidentDate()).isEqualTo(now.minusMonths(4));
        assertThat(content.get(4).getAccidentDate()).isEqualTo(now.minusMonths(5));
    }

    @Test
    public void findAccidentDetailRespDtoByWaterPlaceId_날짜조건_test() {

        // given
        LocalDate now = LocalDate.now();
        Long waterPlaceId = 1L;
        RetrieveAccidentCondition retrieveAccidentCondition = new RetrieveAccidentCondition(now.minusMonths(14), null);
        PageRequest pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "accidentDate"));

        // when
        List<AccidentDetailRespDto> content = accidentRepository.findAccidentDetailRespDtoByWaterPlaceId(waterPlaceId, retrieveAccidentCondition, pageable)
                .getContent();

        // then
        assertThat(content.size()).isEqualTo(3);
        assertThat(content.get(0).getAccidentDate()).isEqualTo(now.minusMonths(14));
        assertThat(content.get(0).getAccidentCondition()).isEqualTo(AccidentEnum.INJURY.getValue());
        assertThat(content.get(0).getPeopleNum()).isEqualTo(10);
        assertThat(content.get(1).getAccidentDate()).isEqualTo(now.minusMonths(14));
        assertThat(content.get(1).getAccidentCondition()).isEqualTo(AccidentEnum.DEATH.getValue());
        assertThat(content.get(1).getPeopleNum()).isEqualTo(3);
        assertThat(content.get(2).getAccidentDate()).isEqualTo(now.minusMonths(14));
        assertThat(content.get(2).getAccidentCondition()).isEqualTo(AccidentEnum.DISAPPEARANCE.getValue());
        assertThat(content.get(2).getPeopleNum()).isEqualTo(32);
    }

    @Test
    public void findAccidentDetailRespDtoByWaterPlaceId_상태조건_test() {

        // given
        LocalDate now = LocalDate.now();
        Long waterPlaceId = 1L;
        RetrieveAccidentCondition retrieveAccidentCondition = new RetrieveAccidentCondition(null, AccidentEnum.DISAPPEARANCE);
        PageRequest pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "accidentDate"));

        // when
        List<AccidentDetailRespDto> content = accidentRepository.findAccidentDetailRespDtoByWaterPlaceId(waterPlaceId, retrieveAccidentCondition, pageable)
                .getContent();

        // then
        assertThat(content.size()).isEqualTo(4);
        assertThat(content.get(0).getAccidentDate()).isEqualTo(now.minusMonths(2));
        assertThat(content.get(0).getAccidentCondition()).isEqualTo(AccidentEnum.DISAPPEARANCE.getValue());
        assertThat(content.get(0).getPeopleNum()).isEqualTo(10);
        assertThat(content.get(1).getAccidentDate()).isEqualTo(now.minusMonths(3));
        assertThat(content.get(1).getAccidentCondition()).isEqualTo(AccidentEnum.DISAPPEARANCE.getValue());
        assertThat(content.get(1).getPeopleNum()).isEqualTo(10);
        assertThat(content.get(2).getAccidentDate()).isEqualTo(now.minusMonths(13));
        assertThat(content.get(2).getAccidentCondition()).isEqualTo(AccidentEnum.DISAPPEARANCE.getValue());
        assertThat(content.get(2).getPeopleNum()).isEqualTo(10);
        assertThat(content.get(3).getAccidentDate()).isEqualTo(now.minusMonths(14));
        assertThat(content.get(3).getAccidentCondition()).isEqualTo(AccidentEnum.DISAPPEARANCE.getValue());
        assertThat(content.get(3).getPeopleNum()).isEqualTo(32);
    }

    @Test
    public void findAccidentDetailRespDtoByWaterPlaceId_날짜조건_상태조건_test() {

        // given
        LocalDate now = LocalDate.now();
        Long waterPlaceId = 1L;
        RetrieveAccidentCondition retrieveAccidentCondition = new RetrieveAccidentCondition(now.minusMonths(14), AccidentEnum.DISAPPEARANCE);
        PageRequest pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "accidentDate"));

        // when
        List<AccidentDetailRespDto> content = accidentRepository.findAccidentDetailRespDtoByWaterPlaceId(waterPlaceId, retrieveAccidentCondition, pageable)
                .getContent();

        // then
        assertThat(content.size()).isEqualTo(1);
        assertThat(content.get(0).getAccidentDate()).isEqualTo(now.minusMonths(14));
        assertThat(content.get(0).getAccidentCondition()).isEqualTo(AccidentEnum.DISAPPEARANCE.getValue());
        assertThat(content.get(0).getPeopleNum()).isEqualTo(32);
    }

    private void dataSetting() {

        WaterPlace waterPlace = waterPlaceRepository.save(newWaterPlace("서울계곡", "서울특별시", 4.9, 2010));

        LocalDate now = LocalDate.now();
        List<Accident> accidentList = new ArrayList<>();
        accidentList.add(newAccident(waterPlace, now.minusMonths(1), AccidentEnum.INJURY, 12));
        accidentList.add(newAccident(waterPlace, now.minusMonths(2), AccidentEnum.DISAPPEARANCE, 10));
        accidentList.add(newAccident(waterPlace, now.minusMonths(3), AccidentEnum.DISAPPEARANCE, 10));
        accidentList.add(newAccident(waterPlace, now.minusMonths(4), AccidentEnum.DEATH, 10));
        accidentList.add(newAccident(waterPlace, now.minusMonths(5), AccidentEnum.DEATH, 10));
        accidentList.add(newAccident(waterPlace, now.minusMonths(10), AccidentEnum.INJURY, 10));
        accidentList.add(newAccident(waterPlace, now.minusMonths(11), AccidentEnum.DEATH, 10));
        accidentList.add(newAccident(waterPlace, now.minusMonths(12), AccidentEnum.INJURY, 10));
        accidentList.add(newAccident(waterPlace, now.minusMonths(13), AccidentEnum.DISAPPEARANCE, 10));
        accidentList.add(newAccident(waterPlace, now.minusMonths(14), AccidentEnum.INJURY, 10));
        accidentList.add(newAccident(waterPlace, now.minusMonths(14), AccidentEnum.DEATH, 3));
        accidentList.add(newAccident(waterPlace, now.minusMonths(14), AccidentEnum.DISAPPEARANCE, 32));
        accidentList.add(newAccident(waterPlace, now.minusMonths(15), AccidentEnum.INJURY, 10));
        accidentList.add(newAccident(waterPlace, now.minusMonths(16), AccidentEnum.INJURY, 10));
        accidentList.add(newAccident(waterPlace, now.minusMonths(17), AccidentEnum.INJURY, 10));
        accidentList.add(newAccident(waterPlace, now.minusMonths(18), AccidentEnum.INJURY, 10));
        accidentList.add(newAccident(waterPlace, now.minusMonths(19), AccidentEnum.INJURY, 10));
        accidentList.add(newAccident(waterPlace, now.minusMonths(20), AccidentEnum.INJURY, 10));
        accidentRepository.saveAll(accidentList);
    }

    private void autoIncrementReset() {

        em.createNativeQuery("ALTER TABLE water_place ALTER COLUMN water_place_id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE accident ALTER COLUMN accident_id RESTART WITH 1").executeUpdate();
    }
}