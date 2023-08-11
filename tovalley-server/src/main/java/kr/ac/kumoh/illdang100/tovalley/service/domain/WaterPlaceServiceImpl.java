package kr.ac.kumoh.illdang100.tovalley.service.domain;

import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.waterplace.WaterPlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.MainPageRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaterPlaceServiceImpl implements WaterPlaceService {

    private final WaterPlaceRepository waterPlaceRepository;
    private final ReviewRepository reviewRepository;

    /**
     * // 물놀이 장소 리스트 조회 페이지
     * @param retrieveWaterPlacesCondition
     * @param pageable
     * @return
     */
    @Override
    public Page<RetrieveWaterPlaceDto> getWaterPlaces(RetrieveWaterPlacesCondition retrieveWaterPlacesCondition, Pageable pageable) {
        return null;
    }

    @Override
    public List<NationalPopularWaterPlacesDto> getPopularWaterPlaces(String cond) {

        List<NationalPopularWaterPlacesDto> result;

        PageRequest pageRequest = PageRequest.of(0, 4);
        if ("RATING".equals(cond)) {

            // 평점 높은순으로 4개 조회
            result = waterPlaceRepository.findTop4ByOrderByRatingDesc(pageRequest)
                    .stream()
                    .map(wp -> {
                        Long waterPlaceId = wp.getId();
                        String location = wp.getProvince() + " " + wp.getCity();
                        int reviewCnt = reviewRepository.findReviewCnt(waterPlaceId);
                        return new NationalPopularWaterPlacesDto(waterPlaceId, wp.getWaterPlaceName(), location, wp.getRating(), reviewCnt);
                    }).collect(Collectors.toList());
        } else {

            // 리뷰 많은순으로 4개 조회
            result = waterPlaceRepository.findTop4ByOrderByReviewCountDesc(pageRequest)
                    .stream()
                    .map(wp -> {
                        Long waterPlaceId = wp.getId();
                        String location = wp.getProvince() + " " + wp.getCity();
                        int reviewCnt = reviewRepository.findReviewCnt(waterPlaceId);
                        return new NationalPopularWaterPlacesDto(waterPlaceId, wp.getWaterPlaceName(), location, wp.getRating(), reviewCnt);
                    }).collect(Collectors.toList());
        }

        return result;
    }
}
