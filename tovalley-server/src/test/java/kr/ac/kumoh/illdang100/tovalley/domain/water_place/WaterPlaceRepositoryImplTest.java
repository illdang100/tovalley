package kr.ac.kumoh.illdang100.tovalley.domain.water_place;

import kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceReqDto.*;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class WaterPlaceRepositoryImplTest extends DummyObject {
    @Autowired
    private WaterPlaceRepository waterPlaceRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        dataSetting();
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName(value = "평점 높은 순, 검색어 없음")
    public void findWaterPlaceList_평점높은순_test()
    {
        // given
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "rating"));
        RetrieveWaterPlacesCondition reqDto1 = new RetrieveWaterPlacesCondition("경상남도", null);
        RetrieveWaterPlacesCondition reqDto2 = new RetrieveWaterPlacesCondition("경상북도", null);
        RetrieveWaterPlacesCondition reqDto3 = new RetrieveWaterPlacesCondition("전국", null);

        // when
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content1 = waterPlaceRepository.findWaterPlaceList(reqDto1, pageRequest).getContent();
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content2 = waterPlaceRepository.findWaterPlaceList(reqDto2, pageRequest).getContent();
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content3 = waterPlaceRepository.findWaterPlaceList(reqDto3, pageRequest).getContent();

        // then
        assertThat(content1.size()).isEqualTo(4);
        assertThat(content1.get(0).getWaterPlaceName()).isEqualTo("양산계곡");
        assertThat(content1.get(1).getWaterPlaceName()).isEqualTo("진주계곡");
        assertThat(content1.get(2).getWaterPlaceName()).isEqualTo("밀양계곡");
        assertThat(content1.get(3).getWaterPlaceName()).isEqualTo("거제계곡");

        assertThat(content2.size()).isEqualTo(4);
        assertThat(content2.get(0).getWaterPlaceName()).isEqualTo("청도계곡");
        assertThat(content2.get(1).getWaterPlaceName()).isEqualTo("안동계곡");
        assertThat(content2.get(2).getWaterPlaceName()).isEqualTo("구미계곡");
        assertThat(content2.get(3).getWaterPlaceName()).isEqualTo("경주계곡");

        assertThat(content3.size()).isEqualTo(5);
        assertThat(content3.get(0).getWaterPlaceName()).isEqualTo("청도계곡");
        assertThat(content3.get(1).getWaterPlaceName()).isEqualTo("양산계곡");
        assertThat(content3.get(2).getWaterPlaceName()).isEqualTo("진주계곡");
        assertThat(content3.get(3).getWaterPlaceName()).isEqualTo("가평계곡");
        assertThat(content3.get(4).getWaterPlaceName()).isEqualTo("제주계곡");
    }

    @Test
    @DisplayName(value = "평점 낮은 순, 검색어 없음")
    public void findWaterPlaceList_평점낮은순_test()
    {
        // given
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "rating"));
        RetrieveWaterPlacesCondition reqDto1 = new RetrieveWaterPlacesCondition("경상남도", null);
        RetrieveWaterPlacesCondition reqDto2 = new RetrieveWaterPlacesCondition("경상북도", null);
        RetrieveWaterPlacesCondition reqDto3 = new RetrieveWaterPlacesCondition("전국", null);

        // when
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content1 = waterPlaceRepository.findWaterPlaceList(reqDto1, pageRequest).getContent();
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content2 = waterPlaceRepository.findWaterPlaceList(reqDto2, pageRequest).getContent();
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content3 = waterPlaceRepository.findWaterPlaceList(reqDto3, pageRequest).getContent();

        // then
        assertThat(content1.size()).isEqualTo(4);
        assertThat(content1.get(3).getWaterPlaceName()).isEqualTo("양산계곡");
        assertThat(content1.get(2).getWaterPlaceName()).isEqualTo("진주계곡");
        assertThat(content1.get(1).getWaterPlaceName()).isEqualTo("밀양계곡");
        assertThat(content1.get(0).getWaterPlaceName()).isEqualTo("거제계곡");

        assertThat(content2.size()).isEqualTo(4);
        assertThat(content2.get(3).getWaterPlaceName()).isEqualTo("청도계곡");
        assertThat(content2.get(2).getWaterPlaceName()).isEqualTo("안동계곡");
        assertThat(content2.get(1).getWaterPlaceName()).isEqualTo("구미계곡");
        assertThat(content2.get(0).getWaterPlaceName()).isEqualTo("경주계곡");

        assertThat(content3.size()).isEqualTo(5);
        assertThat(content3.get(0).getWaterPlaceName()).isEqualTo("부산계곡3?");
        assertThat(content3.get(1).getWaterPlaceName()).isEqualTo("광주계곡3");
        assertThat(content3.get(2).getWaterPlaceName()).isEqualTo("서울계곡2");
        assertThat(content3.get(3).getWaterPlaceName()).isEqualTo("가평계곡3!");
        assertThat(content3.get(4).getWaterPlaceName()).isEqualTo("경주계곡");
    }

    @Test
    @DisplayName(value = "리뷰 많은 순, 검색어 없음")
    public void findWaterPlaceList_리뷰많은순_test()
    {
        // give
        PageRequest pageRequest = PageRequest.of(0, 7, Sort.by(Sort.Direction.DESC, "reviewCount"));
        RetrieveWaterPlacesCondition reqDto1 = new RetrieveWaterPlacesCondition("경상남도", null);
        RetrieveWaterPlacesCondition reqDto2 = new RetrieveWaterPlacesCondition("경상북도", null);
        RetrieveWaterPlacesCondition reqDto3 = new RetrieveWaterPlacesCondition("전국", null);

        // when
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content1 = waterPlaceRepository.findWaterPlaceList(reqDto1, pageRequest).getContent();
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content2 = waterPlaceRepository.findWaterPlaceList(reqDto2, pageRequest).getContent();
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content3 = waterPlaceRepository.findWaterPlaceList(reqDto3, pageRequest).getContent();

        // then
        assertThat(content1.size()).isEqualTo(4);
        assertThat(content1.get(0).getWaterPlaceName()).isEqualTo("밀양계곡");
        assertThat(content1.get(1).getWaterPlaceName()).isEqualTo("거제계곡");
        assertThat(content1.get(2).getWaterPlaceName()).isEqualTo("진주계곡");
        assertThat(content1.get(3).getWaterPlaceName()).isEqualTo("양산계곡");

        assertThat(content2.size()).isEqualTo(4);
        assertThat(content2.get(0).getWaterPlaceName()).isEqualTo("경주계곡");
        assertThat(content2.get(1).getWaterPlaceName()).isEqualTo("구미계곡");
        assertThat(content2.get(2).getWaterPlaceName()).isEqualTo("안동계곡");
        assertThat(content2.get(3).getWaterPlaceName()).isEqualTo("청도계곡");

        assertThat(content3.size()).isEqualTo(7);
        assertThat(content3.get(0).getWaterPlaceName()).isEqualTo("가평계곡2!");
        assertThat(content3.get(1).getWaterPlaceName()).isEqualTo("제천계곡");
        assertThat(content3.get(2).getWaterPlaceName()).isEqualTo("밀양계곡");
        assertThat(content3.get(3).getWaterPlaceName()).isEqualTo("광주계곡");
        assertThat(content3.get(4).getWaterPlaceName()).isEqualTo("거제계곡");
        assertThat(content3.get(5).getWaterPlaceName()).isEqualTo("경주계곡");
        assertThat(content3.get(6).getWaterPlaceName()).isEqualTo("서울계곡");
    }

    @Test
    @DisplayName(value = "평점 높은 순, 검색어 있음")
    public void findWaterPlaceList_평점높은순_검색어_test() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "rating"));
        RetrieveWaterPlacesCondition reqDto1 = new RetrieveWaterPlacesCondition("경기도", "!");
        RetrieveWaterPlacesCondition reqDto2 = new RetrieveWaterPlacesCondition("부산광역시", "?");
        RetrieveWaterPlacesCondition reqDto3 = new RetrieveWaterPlacesCondition("전국", "3");

        // when
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content1 = waterPlaceRepository.findWaterPlaceList(reqDto1, pageRequest).getContent();
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content2 = waterPlaceRepository.findWaterPlaceList(reqDto2, pageRequest).getContent();
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content3 = waterPlaceRepository.findWaterPlaceList(reqDto3, pageRequest).getContent();

        // then
        assertThat(content1.size()).isEqualTo(2);
        assertThat(content1.get(0).getWaterPlaceName()).isEqualTo("가평계곡2!");
        assertThat(content1.get(1).getWaterPlaceName()).isEqualTo("가평계곡3!");

        assertThat(content2.size()).isEqualTo(2);
        assertThat(content2.get(0).getWaterPlaceName()).isEqualTo("부산계곡2?");
        assertThat(content2.get(1).getWaterPlaceName()).isEqualTo("부산계곡3?");

        assertThat(content3.size()).isEqualTo(5);
        assertThat(content3.get(0).getWaterPlaceName()).isEqualTo("서울계곡3");
        assertThat(content3.get(1).getWaterPlaceName()).isEqualTo("제주계곡3");
        assertThat(content3.get(2).getWaterPlaceName()).isEqualTo("가평계곡3!");
        assertThat(content3.get(3).getWaterPlaceName()).isEqualTo("광주계곡3");
        assertThat(content3.get(4).getWaterPlaceName()).isEqualTo("부산계곡3?");
    }
    @Test
    @DisplayName(value = "평점 낮은 순, 검색어 있음")
    public void findWaterPlaceList_평점낮은순_검색어_test() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "rating"));
        RetrieveWaterPlacesCondition reqDto1 = new RetrieveWaterPlacesCondition("경기도", "!");
        RetrieveWaterPlacesCondition reqDto2 = new RetrieveWaterPlacesCondition("부산광역시", "?");
        RetrieveWaterPlacesCondition reqDto3 = new RetrieveWaterPlacesCondition("전국", "3");

        // when
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content1 = waterPlaceRepository.findWaterPlaceList(reqDto1, pageRequest).getContent();
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content2 = waterPlaceRepository.findWaterPlaceList(reqDto2, pageRequest).getContent();
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content3 = waterPlaceRepository.findWaterPlaceList(reqDto3, pageRequest).getContent();

        // then
        assertThat(content1.size()).isEqualTo(2);
        assertThat(content1.get(0).getWaterPlaceName()).isEqualTo("가평계곡3!");
        assertThat(content1.get(1).getWaterPlaceName()).isEqualTo("가평계곡2!");

        assertThat(content2.size()).isEqualTo(2);
        assertThat(content2.get(0).getWaterPlaceName()).isEqualTo("부산계곡3?");
        assertThat(content2.get(1).getWaterPlaceName()).isEqualTo("부산계곡2?");

        assertThat(content3.size()).isEqualTo(5);
        assertThat(content3.get(0).getWaterPlaceName()).isEqualTo("부산계곡3?");
        assertThat(content3.get(1).getWaterPlaceName()).isEqualTo("광주계곡3");
        assertThat(content3.get(2).getWaterPlaceName()).isEqualTo("가평계곡3!");
        assertThat(content3.get(3).getWaterPlaceName()).isEqualTo("제주계곡3");
        assertThat(content3.get(4).getWaterPlaceName()).isEqualTo("서울계곡3");
    }

    @Test
    @DisplayName(value = "리뷰 많은 순, 검색어 있음")
    public void findWaterPlaceList_리뷰많은순_검색어_test()
    {
        // given
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "reviewCount"));
        RetrieveWaterPlacesCondition reqDto1 = new RetrieveWaterPlacesCondition("경기도", "!");
        RetrieveWaterPlacesCondition reqDto2 = new RetrieveWaterPlacesCondition("부산광역시", "?");
        RetrieveWaterPlacesCondition reqDto3 = new RetrieveWaterPlacesCondition("전국", "2");

        // when
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content1 = waterPlaceRepository.findWaterPlaceList(reqDto1, pageRequest).getContent();
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content2 = waterPlaceRepository.findWaterPlaceList(reqDto2, pageRequest).getContent();
        List<WaterPlaceRespDto.RetrieveWaterPlacesDto> content3 = waterPlaceRepository.findWaterPlaceList(reqDto3, pageRequest).getContent();

        // then
        assertThat(content1.size()).isEqualTo(2);
        assertThat(content1.get(0).getWaterPlaceName()).isEqualTo("가평계곡2!");
        assertThat(content1.get(1).getWaterPlaceName()).isEqualTo("가평계곡3!");

        assertThat(content2.size()).isEqualTo(2);
        assertThat(content2.get(0).getWaterPlaceName()).isEqualTo("부산계곡3?");
        assertThat(content2.get(1).getWaterPlaceName()).isEqualTo("부산계곡2?");

        for (WaterPlaceRespDto.RetrieveWaterPlacesDto w : content3) {
            System.out.println(w.getWaterPlaceName());
        }
        assertThat(content3.size()).isEqualTo(5);
        assertThat(content3.get(0).getWaterPlaceName()).isEqualTo("가평계곡2!");
        assertThat(content3.get(1).getWaterPlaceName()).isEqualTo("광주계곡2");
        assertThat(content3.get(2).getWaterPlaceName()).isEqualTo("제주계곡2");
        assertThat(content3.get(3).getWaterPlaceName()).isEqualTo("부산계곡2?");
        assertThat(content3.get(4).getWaterPlaceName()).isEqualTo("서울계곡2");

    }

    private void dataSetting() {

        waterPlaceRepository.save(newWaterPlace("서울계곡", "서울특별시", 4.0, 10));
        waterPlaceRepository.save(newWaterPlace("부산계곡", "부산광역시", 4.1, 9));
        waterPlaceRepository.save(newWaterPlace("구미계곡", "경상북도", 4.2, 8));
        waterPlaceRepository.save(newWaterPlace("안동계곡", "경상북도", 4.3, 7));
        waterPlaceRepository.save(newWaterPlace("여수계곡", "전라남도", 4.4, 6));
        waterPlaceRepository.save(newWaterPlace("제주계곡", "제주특별시", 4.5, 5));
        waterPlaceRepository.save(newWaterPlace("가평계곡", "경기도", 4.6, 4));
        waterPlaceRepository.save(newWaterPlace("진주계곡", "경상남도", 4.7, 3));
        waterPlaceRepository.save(newWaterPlace("양산계곡", "경상남도", 4.8, 2));
        waterPlaceRepository.save(newWaterPlace("청도계곡", "경상북도", 4.9, 1));
        waterPlaceRepository.save(newWaterPlace("제천계곡", "충청북도", 3.9, 15));
        waterPlaceRepository.save(newWaterPlace("밀양계곡", "경상남도", 3.8, 14));
        waterPlaceRepository.save(newWaterPlace("광주계곡", "전라남도", 3.7, 13));
        waterPlaceRepository.save(newWaterPlace("거제계곡", "경상남도", 3.6, 12));
        waterPlaceRepository.save(newWaterPlace("경주계곡", "경상북도", 3.5, 11));

        waterPlaceRepository.save(newWaterPlace("서울계곡2", "서울특별시", 3.0, 1));
        waterPlaceRepository.save(newWaterPlace("서울계곡3", "서울특별시", 3.8, 5));
        waterPlaceRepository.save(newWaterPlace("부산계곡2?", "부산광역시", 3.7, 2));
        waterPlaceRepository.save(newWaterPlace("부산계곡3?", "부산광역시", 2.9, 6));
        waterPlaceRepository.save(newWaterPlace("제주계곡2", "제주특별시", 4.0, 3));
        waterPlaceRepository.save(newWaterPlace("제주계곡3", "제주특별시", 3.6, 10));
        waterPlaceRepository.save(newWaterPlace("가평계곡2!", "경기도", 4.0, 22));
        waterPlaceRepository.save(newWaterPlace("가평계곡3!", "경기도", 3.1, 4));
        waterPlaceRepository.save(newWaterPlace("광주계곡2", "전라남도", 4.0, 10));
        waterPlaceRepository.save(newWaterPlace("광주계곡3", "전라남도", 3.0, 8));
    }
}