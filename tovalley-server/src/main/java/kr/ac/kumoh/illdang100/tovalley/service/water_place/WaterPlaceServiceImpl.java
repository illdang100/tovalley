package kr.ac.kumoh.illdang100.tovalley.service.water_place;

import java.util.Comparator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import kr.ac.kumoh.illdang100.tovalley.domain.Coordinate;
import kr.ac.kumoh.illdang100.tovalley.domain.FileRootPathVO;
import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import kr.ac.kumoh.illdang100.tovalley.domain.review.Review;
import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.WaterQualityReviewEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.*;
import kr.ac.kumoh.illdang100.tovalley.form.water_place.CreateWaterPlaceForm;
import kr.ac.kumoh.illdang100.tovalley.form.water_place.WaterPlaceForm;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import kr.ac.kumoh.illdang100.tovalley.service.OpenApiServiceImpl;
import kr.ac.kumoh.illdang100.tovalley.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.ac.kumoh.illdang100.tovalley.dto.rescue_supply.RescueSupplyRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.rescue_supply.RescueSupplyRespDto.RescueSupplyByWaterPlaceRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.AdminWaterPlaceDetailRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.WaterPlaceDetailRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaterPlaceServiceImpl implements WaterPlaceService {

    private static final String RATING_STR = "RATING";
    private static final String REVIEW_STR = "REVIEW";

    private final WaterPlaceRepository waterPlaceRepository;
    private final WaterPlaceDetailRepository waterPlaceDetailRepository;
    private final RescueSupplyRepository rescueSupplyRepository;
    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;
    private final OpenApiServiceImpl openApiService;
    private final PopularWaterPlacesRedisRepository popularWaterPlacesRedisRepository;

    /**
     * // 물놀이 장소 리스트 조회 페이지
     *
     * @param retrieveWaterPlacesCondition
     * @return
     */
    @Override
    public Page<RetrieveWaterPlacesDto> getWaterPlaces(RetrieveWaterPlacesCondition retrieveWaterPlacesCondition) {

        PageRequest pageRequest = PageRequest.of(retrieveWaterPlacesCondition.getPage(), 12,
                Sort.by(Sort.Direction.DESC, retrieveWaterPlacesCondition.getSortCond()));

        return waterPlaceRepository.findWaterPlaceList(retrieveWaterPlacesCondition, pageRequest);
    }

    @Override
    @Scheduled(cron = "0 */10 * * * *") // 10분마다 실행
    public void schedulePopularWaterPlaces() {
        log.info("인기 물놀이 장소 통계 계산중!!");
        Pageable pageable = PageRequest.of(0, 8);
        List<WaterPlace> waterPlacesByRating = getTop8WaterPlacesByRating(pageable);
        List<WaterPlace> waterPlacesByReview = getTop8WaterPlacesByReview(pageable);

        popularWaterPlacesRedisRepository.deleteAll();
        savePopularWaterPlaces(waterPlacesByRating, RATING_STR);
        savePopularWaterPlaces(waterPlacesByReview, REVIEW_STR);
        log.info("인기 물놀이 장소 통계 계산 완료!!");
    }

    private List<WaterPlace> getTop8WaterPlacesByRating(Pageable pageable) {
        return waterPlaceRepository.findTop8ByOrderByRatingDesc(pageable);
    }

    private List<WaterPlace> getTop8WaterPlacesByReview(Pageable pageable) {
        return waterPlaceRepository.findTop8ByOrderByReviewCountDesc(pageable);
    }

    private void savePopularWaterPlaces(List<WaterPlace> waterPlaces, String condition) {
        List<PopularWaterPlaces> popularWaterPlaces = mapToPopularWaterPlaces(waterPlaces, condition);
        popularWaterPlacesRedisRepository.saveAll(popularWaterPlaces);
    }

    private List<PopularWaterPlaces> mapToPopularWaterPlaces(List<WaterPlace> waterPlaces, String condition) {
        return waterPlaces.stream()
                .map(wp -> createPopularWaterPlace(wp, condition))
                .collect(Collectors.toList());
    }

    private PopularWaterPlaces createPopularWaterPlace(WaterPlace wp, String cond) {
        String location = wp.getProvince() + " " + wp.getCity();
        String formattedRating = getFormattedRating(wp.getRating());
        String imageUrl = getImageUrl(wp.getWaterPlaceImage());

        return new PopularWaterPlaces(wp.getId(), wp.getWaterPlaceName(), location, formattedRating,
                wp.getReviewCount(), imageUrl, cond);
    }

    private String getImageUrl(ImageFile waterPlaceImage) {
        return (waterPlaceImage == null) ? null : waterPlaceImage.getStoreFileUrl();
    }


    private String getFormattedRating(Double rating) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(rating);
    }

    /**
     * @methodnme: getPopularWaterPlaces
     * @author: JYeonJun
     * @description: 전국 인기 계곡 리스트(8개) 조회 메서드
     * @return: 인기 계곡 내림차순(평점 or 리뷰수) 정보
     */
    @Override
    public List<NationalPopularWaterPlacesDto> getPopularWaterPlaces(String cond) {
        List<PopularWaterPlaces> popularWaterPlacesList = getPopularWaterPlacesList();

        Stream<PopularWaterPlaces> filteredPopularWaterPlacesStream = filterByCondition(popularWaterPlacesList, cond);

        Comparator<PopularWaterPlaces> comparator = createComparator(cond);

        return getSortedAndMappedWaterPlaces(filteredPopularWaterPlacesStream, comparator);
    }

    private List<PopularWaterPlaces> getPopularWaterPlacesList() {
        return StreamSupport.stream(popularWaterPlacesRedisRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    private Stream<PopularWaterPlaces> filterByCondition(List<PopularWaterPlaces> waterPlacesList, String cond) {
        return waterPlacesList.stream()
                .filter(popularWaterPlaces -> cond.equals(popularWaterPlaces.getCondition()));
    }

    private Comparator<PopularWaterPlaces> createComparator(String cond) {
        if (RATING_STR.equals(cond)) {
            return Comparator.comparing(PopularWaterPlaces::getRating)
                    .thenComparing(PopularWaterPlaces::getReviewCnt)
                    .reversed();
        }

        if (REVIEW_STR.equals(cond)) {
            return Comparator.comparing(PopularWaterPlaces::getReviewCnt)
                    .thenComparing(PopularWaterPlaces::getRating)
                    .reversed();
        }

        return null;
    }

    private List<NationalPopularWaterPlacesDto> getSortedAndMappedWaterPlaces(Stream<PopularWaterPlaces> waterPlacesStream, Comparator<PopularWaterPlaces> comparator) {
        return waterPlacesStream
                .sorted(comparator)
                .map(this::mapToPopularWaterPlaceDto)
                .collect(Collectors.toList());
    }

    private NationalPopularWaterPlacesDto mapToPopularWaterPlaceDto(PopularWaterPlaces wp) {
        return new NationalPopularWaterPlacesDto(wp.getWaterPlaceId(), wp.getWaterPlaceName(), wp.getLocation(), wp.getRating(),
                wp.getReviewCnt(), wp.getWaterPlaceImageUrl());
    }


    /**
     * @param waterPlaceId: 물놀이 장소 pk
     * @methodnme: getWaterPlaceDetailByWaterPlace
     * @author: JYeonJun
     * @description: 물놀이 장소 상세정보 조회
     * @return: 물놀이 장소 상세정보
     */
    @Override
    public WaterPlaceDetailRespDto getWaterPlaceDetailByWaterPlace(Long waterPlaceId) {
        WaterPlaceDetail findWaterPlaceDetail =
                findWaterPlaceDetailWithWaterPlaceByWaterPlaceIdOrElseThrowEx(waterPlaceDetailRepository, waterPlaceId);

        List<Review> findReviews = reviewRepository.findAllByWaterPlace_Id(waterPlaceId);
        Map<String, Integer> reviewCounts = countReviewOccurrences(findReviews);

        WaterPlace findWaterPlace = findWaterPlaceDetail.getWaterPlace();
        Coordinate coordinate = findWaterPlace.getCoordinate();

        return createWaterPlaceDetailRespDto(findWaterPlace, coordinate, findWaterPlaceDetail, reviewCounts);
    }

    private Map<String, Integer> countReviewOccurrences(List<Review> reviews) {
        Map<String, Integer> reviewCounts = new HashMap<>();

        for (Review review : reviews) {
            WaterQualityReviewEnum reviewEnum = review.getWaterQualityReview();

            reviewCounts.put(reviewEnum.getValue(), reviewCounts.getOrDefault(reviewEnum.getValue(), 0) + 1);
        }

        return reviewCounts;
    }

    /**
     * @param waterPlaceId: 물놀이 장소 pk
     * @methodnme: getRescueSuppliesByWaterPlace
     * @author: JYeonJun
     * @description: 물놀이 장소에 배치된 구조용품 현황 조회
     * @return: 구조용품 수량
     */
    @Override
    public RescueSupplyByWaterPlaceRespDto getRescueSuppliesByWaterPlace(Long waterPlaceId) {

        RescueSupply findRescueSupply = findRescueSupplyByWaterPlaceIdOrElseThrowEx(rescueSupplyRepository,
                waterPlaceId);

        return createRescueSupplyRespDto(findRescueSupply);
    }

    /**
     * @param waterPlaceId: 물놀이 장소 pk
     * @methodnme: getAdminWaterPlaceDetailByWaterPlace
     * @author: JYeonJun
     * @description: 관리자용 물놀이 장소 상세보기 페이지 정보 조회
     * @return: 관리자용 물놀이 장소 상세정보
     */
    @Override
    public AdminWaterPlaceDetailRespDto getAdminWaterPlaceDetailByWaterPlace(Long waterPlaceId) {

        WaterPlaceDetail findWaterPlaceDetail =
                findWaterPlaceDetailWithWaterPlaceByWaterPlaceIdOrElseThrowEx(waterPlaceDetailRepository, waterPlaceId);

        WaterPlace findWaterPlace = findWaterPlaceDetail.getWaterPlace();
        Coordinate coordinate = findWaterPlace.getCoordinate();

        return createAdminWaterPlaceDetailRespDto(findWaterPlace, coordinate, findWaterPlaceDetail);
    }

    /**
     * @param form: 수정된 물놀이 장소 정보
     * @methodnme: updateWaterPlace
     * @author: JYeonJun
     * @description: 물놀이 장소 정보 업데이트
     * @return:
     */
    @Override
    @Transactional
    public void updateWaterPlace(Long waterPlaceId, WaterPlaceForm form) {

        WaterPlace findWaterPlace =
                findWaterPlaceByIdOrElseThrowEx(waterPlaceRepository, waterPlaceId);

        WaterPlaceDetail findWaterPlaceDetail =
                findWaterPlaceDetailByWaterPlaceIdOrElseThrowEx(waterPlaceDetailRepository, waterPlaceId);

        RescueSupply findRescueSupply =
                findRescueSupplyByWaterPlaceIdOrElseThrowEx(rescueSupplyRepository, waterPlaceId);

        findWaterPlace.update(form);
        findWaterPlaceDetail.update(form);
        findRescueSupply.update(form);
    }

    @Override
    @Transactional
    public void updateWaterPlaceImage(Long waterPlaceId, MultipartFile waterPlaceImage) {
        WaterPlace findWaterPlace = findWaterPlaceByIdOrElseThrowEx(waterPlaceRepository, waterPlaceId);
        processImageUpdate(findWaterPlace, waterPlaceImage);
    }

    /**
     * @param form: 새로운 물놀이 장소 정보
     * @methodnme: saveNewWaterPlace
     * @author: JYeonJun
     * @description: 새로운 물놀이 장소 데이터베이스에 등록
     * @return:
     */
    @Override
    @Transactional
    public void saveNewWaterPlace(CreateWaterPlaceForm form) {

        Coordinate coordinate = openApiService.getGeoDataByAddress(form.getAddress());

        WaterPlace newWaterPlace =
                WaterPlace.createNewWaterPlace(form, coordinate);
        WaterPlaceDetail newWaterPlaceDetail =
                WaterPlaceDetail.createNewWaterPlaceDetail(newWaterPlace, form);
        RescueSupply newRescueSupply =
                RescueSupply.createNewRescueSupply(newWaterPlace, form);

        processImageUpdate(newWaterPlace, form.getWaterPlaceImage());

        waterPlaceRepository.save(newWaterPlace);
        waterPlaceDetailRepository.save(newWaterPlaceDetail);
        rescueSupplyRepository.save(newRescueSupply);
    }

    /**
     * @param searchWord: 검색어
     * @methodnme: getAdminWaterPlaceList
     * @author: jimin
     * @description: 관리자용 물놀이 장소 리스트 조회 페이지
     * @return: 관리자용 물놀이 장소 리스트
     */
    @Override
    public Page<AdminRetrieveWaterPlacesDto> getAdminWaterPlaceList(String searchWord, Pageable pageable) {
        return waterPlaceRepository.getAdminWaterPlaceList(searchWord, pageable);
    }

    private void processImageUpdate(WaterPlace waterPlace, MultipartFile waterPlaceImage) {
        try {
            if (waterPlaceImage != null) {
                ImageFile createdImageFile = s3Service.upload(waterPlaceImage, FileRootPathVO.WATER_PLACE_PATH);
                deleteWaterPlaceImage(waterPlace);
                waterPlace.changeWaterPlaceImage(createdImageFile);
            } else {
                deleteWaterPlaceImage(waterPlace);
            }
        } catch (Exception e) {
            throw new CustomApiException(e.getMessage());
        }
    }

    private void deleteWaterPlaceImage(WaterPlace waterPlace) {
        if (hasAccountProfileImage(waterPlace)) {
            try {
                s3Service.delete(waterPlace.getWaterPlaceImage().getStoreFileName());
                waterPlace.changeWaterPlaceImage(null);
            } catch (Exception e) {
                throw new CustomApiException(e.getMessage());
            }
        }
    }

    private boolean hasAccountProfileImage(WaterPlace waterPlace) {
        return waterPlace.getWaterPlaceImage() != null;
    }
}
