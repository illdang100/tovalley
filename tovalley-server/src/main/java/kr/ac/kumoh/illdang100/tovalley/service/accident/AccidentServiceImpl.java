package kr.ac.kumoh.illdang100.tovalley.service.accident;

import kr.ac.kumoh.illdang100.tovalley.domain.accident.Accident;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentRepository;
import kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import static kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentRespDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccidentServiceImpl implements AccidentService {

    private final AccidentRepository accidentRepository;

    @Override
    public Accident addAccident(Long waterPlaceId) {
        return null;
    }

    @Override
    public void deleteAccident(Long accidentId) {

    }

    /**
     * @methodnme: getAccidentCntPerMonthByProvince
     * @author: JYeonJun
     * @description: 지역별(행정자치구) 사건사고 수 조회
     *
     * @return: 달별 사망, 실종, 부상 사고수 정보 조회 및 총 사망, 실종, 부상 사고수 정보 조회
     */
    @Override
    public AccidentCountDto getAccidentCntPerMonthByProvince(String province) {

        List<Accident> accidents = getAllAccidentsByProvince(province);

        Map<Integer, AccidentCountPerMonthDto> accidentCountMap = initializeAccidentCountMap();

        calculateAccidentCounts(accidents, accidentCountMap);

        List<AccidentCountPerMonthDto> accidentCountPerMonthList = new ArrayList<>(accidentCountMap.values());

        Integer totalDeathCnt =
                calculateTotalFromAccidentCountPerMonthDto(accidentCountPerMonthList, AccidentCountPerMonthDto::getDeathCnt);
        Integer totalDisappearanceCnt =
                calculateTotalFromAccidentCountPerMonthDto(accidentCountPerMonthList, AccidentCountPerMonthDto::getDisappearanceCnt);
        Integer totalInjuryCnt =
                calculateTotalFromAccidentCountPerMonthDto(accidentCountPerMonthList, AccidentCountPerMonthDto::getInjuryCnt);

        return new AccidentCountDto(accidentCountPerMonthList, province, totalDeathCnt, totalDisappearanceCnt, totalInjuryCnt);
    }

    private List<Accident> getAllAccidentsByProvince(String province) {
        if ("전국".equals(province)) {
            return accidentRepository.findAll();
        } else {
            return accidentRepository.findByProvinceStartingWithProvince(province);
        }
    }

    private void calculateAccidentCounts(List<Accident> accidents, Map<Integer, AccidentCountPerMonthDto> accidentCountMap) {
        for (Accident accident : accidents) {
            Integer month = accident.getAccidentDate().getMonthValue();
            AccidentCountPerMonthDto accidentCountDto = accidentCountMap.get(month);

            switch (accident.getAccidentCondition()) {
                case DEATH:
                    accidentCountDto.incrementDeathCnt(accident.getPeopleNum());
                    break;
                case DISAPPEARANCE:
                    accidentCountDto.incrementDisappearanceCnt(accident.getPeopleNum());
                    break;
                case INJURY:
                    accidentCountDto.incrementInjuryCnt(accident.getPeopleNum());
                    break;
                default:
                    break;
            }
        }
    }

    private Map<Integer, AccidentCountPerMonthDto> initializeAccidentCountMap() {
        Map<Integer, AccidentCountPerMonthDto> accidentCountMap = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            accidentCountMap.put(month, new AccidentCountPerMonthDto(month, 0, 0, 0));
        }
        return accidentCountMap;
    }

    private Integer calculateTotalFromAccidentCountPerMonthDto(List<AccidentCountPerMonthDto> list, ToIntFunction<AccidentCountPerMonthDto> mapper) {
        return list.stream().mapToInt(mapper).sum();
    }

    /**
     * @methodnme: getAccidentsFor5YearsByWaterPlace
     * @author: JYeonJun
     * @param waterPlaceId: 물놀이 장소 pk
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

    @Override
    public AccidentForAdminByWaterPlace getAccidentDetailByWaterPlace(Long waterPlaceId,
                                                                      RetrieveAccidentCondition retrieveAccidentCondition,
                                                                      Pageable pageable) {

        Page<AccidentDetailRespDto> accidentDetail =
                accidentRepository.findAccidentDetailRespDtoByWaterPlaceId(waterPlaceId, retrieveAccidentCondition, pageable);

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
}
