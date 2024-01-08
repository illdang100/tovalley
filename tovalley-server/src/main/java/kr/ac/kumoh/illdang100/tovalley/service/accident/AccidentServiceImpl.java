package kr.ac.kumoh.illdang100.tovalley.service.accident;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import kr.ac.kumoh.illdang100.tovalley.domain.ProvinceEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.Accident;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.RegionAccidentStatistics;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.RegionAccidentStatistics.AccidentCountPerMonth;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.RegionAccidentStatisticsRedisRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.form.accident.CreateAccidentForm;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;

import static kr.ac.kumoh.illdang100.tovalley.domain.ProvinceEnum.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccidentServiceImpl implements AccidentService {

    private final AccidentRepository accidentRepository;
    private final WaterPlaceRepository waterPlaceRepository;
    private final RegionAccidentStatisticsRedisRepository regionAccidentStatisticsRedisRepository;

    @Override
    @Transactional
    public void saveNewAccident(Long waterPlaceId, CreateAccidentForm form) {

        WaterPlace findWaterPlace =
                findWaterPlaceByIdOrElseThrowEx(waterPlaceRepository, waterPlaceId);

        accidentRepository.save(Accident.builder()
                .waterPlace(findWaterPlace)
                .accidentDate(form.getAccidentDate())
                .accidentCondition(form.getAccidentCondition())
                .peopleNum(form.getPeopleNum())
                .build());
    }

    @Override
    @Transactional
    public void deleteAccident(Long waterPlaceId, Long accidentId) {

        Accident findAccident =
                findAccidentByIdAndWaterPlaceIdOrElseThrowEx(accidentRepository, accidentId, waterPlaceId);

        accidentRepository.delete(findAccident);
    }

    /**
     * @methodnme: getAccidentCntPerMonthByProvince
     * @author: JYeonJun
     * @description: 지역별(행정자치구) 사건사고 수 조회
     * @return: 달별 사망, 실종, 부상 사고수 정보 조회 및 총 사망, 실종, 부상 사고수 정보 조회
     */
    @Override
    public AccidentCountDto getAccidentCntPerMonthByProvince(String province) {
        RegionAccidentStatistics findRegionAccidentStatistics
                = regionAccidentStatisticsRedisRepository.findByProvince(province)
                .orElseThrow(() -> new CustomApiException(province + "에 대한 사건사고 통계가 존재하지 않습니다"));

        List<AccidentCountPerMonthDto> accidentCountPerMonthDtoList
                = convertToDtoList(findRegionAccidentStatistics.getAccidentCountPerMonth());

        return new AccidentCountDto(
                accidentCountPerMonthDtoList,
                findRegionAccidentStatistics.getProvince(),
                findRegionAccidentStatistics.getTotalDeathCnt(),
                findRegionAccidentStatistics.getTotalDisappearanceCnt(),
                findRegionAccidentStatistics.getTotalInjuryCnt()
        );
    }

    private List<AccidentCountPerMonthDto> convertToDtoList(List<AccidentCountPerMonth> accidentCountPerMonthList) {
        return accidentCountPerMonthList
                .stream()
                .map(AccidentCountPerMonthDto::new)
                .collect(Collectors.toList());
    }


    /**
     * @param waterPlaceId: 물놀이 장소 pk
     * @methodnme: getAccidentsFor5YearsByWaterPlace
     * @author: JYeonJun
     * @description: 물놀이 장소에서 발생한 최근 5년간 사건사고 현황 정보 조회
     * @return: 사건사고 현황 정보
     */
    @Override
    public WaterPlaceAccidentFor5YearsDto getAccidentsFor5YearsByWaterPlace(Long waterPlaceId) {
        LocalDate fiveYearsAgo = LocalDate.now().minusYears(5);

        List<Accident> accidents =
                accidentRepository.findByWaterPlaceIdAndAccidentDateAfter(waterPlaceId, fiveYearsAgo);

        int totalDeathCnt = 0;
        int totalDisappearanceCnt = 0;
        int totalInjuryCnt = 0;

        for (Accident accident : accidents) {
            switch (accident.getAccidentCondition()) {
                case DEATH:
                    totalDeathCnt += accident.getPeopleNum();
                    break;
                case DISAPPEARANCE:
                    totalDisappearanceCnt += accident.getPeopleNum();
                    break;
                case INJURY:
                    totalInjuryCnt += accident.getPeopleNum();
                    break;
            }
        }

        return new WaterPlaceAccidentFor5YearsDto(totalDeathCnt, totalDisappearanceCnt, totalInjuryCnt);
    }

    /**
     * @param waterPlaceId:              물놀이 장소 pk
     * @param retrieveAccidentCondition: 사건/사고 검색 조건
     * @param pageable:                  페이징 정보
     * @methodnme: getAccidentDetailByWaterPlace
     * @author: JYeonJun
     * @description: 물놀이 장소에 대한 사건/사고 조회
     * @return: 물놀이 장소에 대한 사고 정보
     */
    @Override
    public AccidentForAdminByWaterPlace getAccidentDetailByWaterPlace(Long waterPlaceId,
                                                                      RetrieveAccidentCondition retrieveAccidentCondition,
                                                                      Pageable pageable) {

        Page<AccidentDetailRespDto> accidentDetail =
                accidentRepository.findAccidentDetailRespDtoByWaterPlaceId(waterPlaceId, retrieveAccidentCondition,
                        pageable);

        List<Accident> accidentsByWaterPlace = accidentRepository.findByWaterPlaceId(waterPlaceId);

        int totalDeathCnt = calculateTotalFromAccident(accidentsByWaterPlace, AccidentEnum.DEATH);
        int totalDisappearanceCnt = calculateTotalFromAccident(accidentsByWaterPlace, AccidentEnum.DISAPPEARANCE);
        int totalInjuryCnt = calculateTotalFromAccident(accidentsByWaterPlace, AccidentEnum.INJURY);

        return new AccidentForAdminByWaterPlace(accidentDetail, totalDeathCnt, totalDisappearanceCnt, totalInjuryCnt);
    }

    private int calculateTotalFromAccident(List<Accident> accidents, AccidentEnum condition) {
        return accidents.stream()
                .filter(accident -> accident.getAccidentCondition() == condition)
                .mapToInt(Accident::getPeopleNum)
                .sum();
    }

    /**
     * @methodnme: scheduleAccidentStatisticsByRegion
     * @author: JYeonJun
     * @description: 행정구역별 사건,사고 수를 통계내서 Redis에 저장하는 작업 스케줄링
     */
    @Override
    @Scheduled(cron = "0 0/30 * * * ?")
    public void scheduleAccidentStatisticsByRegion() {
        log.info("사건,사고 통계 계산중!!");
        Map<ProvinceEnum, RegionAccidentStatistics> updatedStatistics = new HashMap<>();

        RegionAccidentStatistics nationwideStatistic = createNationwideStatistic();

        calculateAccidentStatistics(updatedStatistics, nationwideStatistic);

        regionAccidentStatisticsRedisRepository.deleteAll();
        regionAccidentStatisticsRedisRepository.saveAll(updatedStatistics.values());
        log.info("사건,사고 통계 계산 완료!!");
    }

    private RegionAccidentStatistics createNationwideStatistic() {
        return RegionAccidentStatistics.builder()
                .province(NATIONWIDE.getValue())
                .accidentCountPerMonth(new ArrayList<>())
                .totalDeathCnt(0)
                .totalDisappearanceCnt(0)
                .totalInjuryCnt(0)
                .build();
    }

    private void calculateAccidentStatistics(Map<ProvinceEnum, RegionAccidentStatistics> updatedStatistics,
                                             RegionAccidentStatistics nationwideStatistic) {
        for (ProvinceEnum provinceEnum : values()) {
            if (provinceEnum.equals(NATIONWIDE)) {
                continue;
            }
            RegionAccidentStatistics regionAccidentStatistics = generateMonthlyRegionAccidentStatistics(
                    provinceEnum.getValue());
            updatedStatistics.put(provinceEnum, regionAccidentStatistics);
            updateNationwideStatistic(nationwideStatistic, regionAccidentStatistics);
        }

        updatedStatistics.put(ProvinceEnum.NATIONWIDE, nationwideStatistic);
    }

    private void updateNationwideStatistic(RegionAccidentStatistics nationwideStatistic,
                                           RegionAccidentStatistics regionAccidentStatistics) {
        nationwideStatistic.incrementTotalDeathCnt(regionAccidentStatistics.getTotalDeathCnt());
        nationwideStatistic.incrementTotalDisappearanceCnt(regionAccidentStatistics.getTotalDisappearanceCnt());
        nationwideStatistic.incrementTotalInjuryCnt(regionAccidentStatistics.getTotalInjuryCnt());

        updateNationwideAccidentCount(nationwideStatistic, regionAccidentStatistics);
    }

    private void updateNationwideAccidentCount(RegionAccidentStatistics nationwideStatistic,
                                               RegionAccidentStatistics regionAccidentStatistics) {
        for (AccidentCountPerMonth accidentCount : regionAccidentStatistics.getAccidentCountPerMonth()) {
            AccidentCountPerMonth nationwideAccidentCount = getOrCreateNationwideAccidentCount(nationwideStatistic,
                    accidentCount);
            nationwideAccidentCount.incrementDeathCnt(accidentCount.getDeathCnt());
            nationwideAccidentCount.incrementDisappearanceCnt(accidentCount.getDisappearanceCnt());
            nationwideAccidentCount.incrementInjuryCnt(accidentCount.getInjuryCnt());
        }
    }

    private AccidentCountPerMonth getOrCreateNationwideAccidentCount(RegionAccidentStatistics nationwideStatistic,
                                                                     AccidentCountPerMonth accidentCount) {
        return nationwideStatistic.getAccidentCountPerMonth().stream()
                .filter(count -> count.getMonth().equals(accidentCount.getMonth()))
                .findAny()
                .orElseGet(() -> createAndAddNewAccidentCount(nationwideStatistic, accidentCount));
    }

    private AccidentCountPerMonth createAndAddNewAccidentCount(RegionAccidentStatistics nationwideStatistic,
                                                               AccidentCountPerMonth accidentCount) {
        AccidentCountPerMonth newCount = new AccidentCountPerMonth(accidentCount);
        nationwideStatistic.getAccidentCountPerMonth().add(newCount);
        return newCount;
    }

    private RegionAccidentStatistics generateMonthlyRegionAccidentStatistics(String province) {
        List<Accident> accidents = retrieveAccidentsByProvinceForLastYear(province);
        Map<Integer, AccidentCountPerMonth> accidentCountMap = createInitialMonthlyAccidentCountMap();

        accumulateAccidentCountsByMonth(accidents, accidentCountMap);

        List<AccidentCountPerMonth> accidentCountPerMonthList = new ArrayList<>(accidentCountMap.values());

        return RegionAccidentStatistics.builder()
                .province(province)
                .accidentCountPerMonth(accidentCountPerMonthList)
                .totalDeathCnt(calculateTotalCount(accidentCountPerMonthList, AccidentCountPerMonth::getDeathCnt))
                .totalDisappearanceCnt(
                        calculateTotalCount(accidentCountPerMonthList, AccidentCountPerMonth::getDisappearanceCnt))
                .totalInjuryCnt(calculateTotalCount(accidentCountPerMonthList, AccidentCountPerMonth::getInjuryCnt))
                .build();
    }

    private List<Accident> retrieveAccidentsByProvinceForLastYear(String province) {
        int year = LocalDate.now().getYear();
        return "전국".equals(province) ? accidentRepository.findByYear(year)
                : accidentRepository.findByProvinceStartingWithProvinceAndYear(province, year);
    }

    private void accumulateAccidentCountsByMonth(List<Accident> accidents,
                                                 Map<Integer, AccidentCountPerMonth> accidentCountMap) {
        for (Accident accident : accidents) {
            Integer month = accident.getAccidentDate().getMonthValue();
            AccidentCountPerMonth accidentCountPerMonth = accidentCountMap.get(month);

            switch (accident.getAccidentCondition()) {
                case DEATH:
                    accidentCountPerMonth.incrementDeathCnt(accident.getPeopleNum());
                    break;
                case DISAPPEARANCE:
                    accidentCountPerMonth.incrementDisappearanceCnt(accident.getPeopleNum());
                    break;
                case INJURY:
                    accidentCountPerMonth.incrementInjuryCnt(accident.getPeopleNum());
                    break;
                default:
                    break;
            }
        }
    }

    private Map<Integer, AccidentCountPerMonth> createInitialMonthlyAccidentCountMap() {
        return IntStream.rangeClosed(1, 12)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), month -> new AccidentCountPerMonth(month, 0, 0, 0)));
    }

    private Integer calculateTotalCount(List<AccidentCountPerMonth> list, ToIntFunction<AccidentCountPerMonth> mapper) {
        return list.stream().mapToInt(mapper).sum();
    }
}
