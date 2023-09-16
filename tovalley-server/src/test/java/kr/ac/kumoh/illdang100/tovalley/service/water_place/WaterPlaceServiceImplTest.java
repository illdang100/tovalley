package kr.ac.kumoh.illdang100.tovalley.service.water_place;

import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.RetrieveWaterPlacesDto;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import kr.ac.kumoh.illdang100.tovalley.service.water_place.WaterPlaceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class WaterPlaceServiceImplTest extends DummyObject {

    @InjectMocks
    private WaterPlaceServiceImpl waterPlaceService;

    @Mock
    private WaterPlaceRepository waterPlaceRepository;

    @Test
    public void getPopularWaterPlaces_테스트() {

        // given
        String cond1 = "RATING";
        String cond2 = "REVIEW";

        List<WaterPlace> waterPlaces1 = new ArrayList<>();
        List<WaterPlace> waterPlaces2 = new ArrayList<>();

        WaterPlace waterPlace1 = newMockWaterPlace(2L, "서울계곡", "서울특별시", 4.9, 2010);
        WaterPlace waterPlace2 = newMockWaterPlace(1L, "금오계곡", "경상북도", 2.0, 10);
        WaterPlace waterPlace3 = newMockWaterPlace(3L, "대전계곡", "대전광역시", 2.5, 130);
        WaterPlace waterPlace4 = newMockWaterPlace(4L, "부산계곡", "부산광역시", 4.0, 410);
        WaterPlace waterPlace5 = newMockWaterPlace(5L, "울산계곡", "울산광역시", 3.8, 110);
        WaterPlace waterPlace6 = newMockWaterPlace(6L, "대구계곡", "대구광역시", 1.1, 110);
        WaterPlace waterPlace7 = newMockWaterPlace(7L, "울릉계곡", "경상북도", 5.0, 3);
        WaterPlace waterPlace8 = newMockWaterPlace(8L, "제주계곡", "제주특별자치도", 4.1, 1100);

        waterPlaces1.add(waterPlace7);
        waterPlaces1.add(waterPlace1);
        waterPlaces1.add(waterPlace8);
        waterPlaces1.add(waterPlace4);
        waterPlaces1.add(waterPlace5);
        waterPlaces1.add(waterPlace3);
        waterPlaces1.add(waterPlace2);
        waterPlaces1.add(waterPlace6);

        waterPlaces2.add(waterPlace1);
        waterPlaces2.add(waterPlace8);
        waterPlaces2.add(waterPlace4);
        waterPlaces2.add(waterPlace3);
        waterPlaces2.add(waterPlace5);
        waterPlaces2.add(waterPlace6);
        waterPlaces2.add(waterPlace2);
        waterPlaces2.add(waterPlace7);

        // stub1
        when(waterPlaceRepository.findTop8ByOrderByRatingDesc(any())).thenReturn(waterPlaces1);

        // stub2
        when(waterPlaceRepository.findTop8ByOrderByReviewCountDesc(any())).thenReturn(waterPlaces2);

        // when
        List<NationalPopularWaterPlacesDto> result1 = waterPlaceService.getPopularWaterPlaces(cond1);
        List<NationalPopularWaterPlacesDto> result2 = waterPlaceService.getPopularWaterPlaces(cond2);

        // then
        assertThat(result1.get(0).getWaterPlaceName()).isEqualTo("울릉계곡");
        assertThat(result1.get(7).getWaterPlaceName()).isEqualTo("대구계곡");

        assertThat(result2.get(0).getWaterPlaceName()).isEqualTo("서울계곡");
        assertThat(result2.get(7).getWaterPlaceName()).isEqualTo("울릉계곡");
    }

    @Test
    public void findWaterPlaceList_테스트() {

        // given
        List<RetrieveWaterPlacesDto> waterPlaceDtoList = new ArrayList<>();
        RetrieveWaterPlacesDto retrieveWaterPlacesDto1 = new RetrieveWaterPlacesDto(1L, "서울계곡", "종로구", 4.9, 50, "중점관리지역", "계곡", null);
        RetrieveWaterPlacesDto retrieveWaterPlacesDto2 = new RetrieveWaterPlacesDto(1L, "금오계곡", "형곡동", 4.8, 40, "중점관리지역", "계곡", null);
        RetrieveWaterPlacesDto retrieveWaterPlacesDto3 = new RetrieveWaterPlacesDto(1L, "대전계곡", "서구", 4.7, 30, "중점관리지역", "계곡", null);
        RetrieveWaterPlacesDto retrieveWaterPlacesDto4 = new RetrieveWaterPlacesDto(1L, "부산계곡", "중구", 4.6, 20, "중점관리지역", "계곡", null);
        RetrieveWaterPlacesDto retrieveWaterPlacesDto5 = new RetrieveWaterPlacesDto(1L, "울산계곡", "북구", 4.5, 10, "중점관리지역", "계곡", null);

        waterPlaceDtoList.add(retrieveWaterPlacesDto1);
        waterPlaceDtoList.add(retrieveWaterPlacesDto2);
        waterPlaceDtoList.add(retrieveWaterPlacesDto3);
        waterPlaceDtoList.add(retrieveWaterPlacesDto4);
        waterPlaceDtoList.add(retrieveWaterPlacesDto5);

        RetrieveWaterPlacesCondition condition = new RetrieveWaterPlacesCondition("경상북도", null, null, "rating", 0);
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, condition.getSortCond()));
        Page<RetrieveWaterPlacesDto> page = new PageImpl<>(waterPlaceDtoList, pageable, waterPlaceDtoList.size());

        // stub
        when(waterPlaceRepository.findWaterPlaceList(any(), any())).thenReturn(page);

        // when
        Page<RetrieveWaterPlacesDto> waterPlaceList = waterPlaceRepository.findWaterPlaceList(condition, pageable);

        // then
        assertThat(waterPlaceList.getContent()).isEqualTo(page.getContent());
        assertThat(waterPlaceList.getTotalElements()).isEqualTo(page.getTotalElements());
        assertThat(waterPlaceList.getTotalPages()).isEqualTo(page.getTotalPages());
        assertThat(waterPlaceList.getNumber()).isEqualTo(page.getNumber());
        assertThat(waterPlaceList.getSize()).isEqualTo(page.getSize());
    }
}