package kr.ac.kumoh.illdang100.tovalley.service.domain.accident;

import kr.ac.kumoh.illdang100.tovalley.domain.accident.Accident;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.MainPageRespDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccidentServiceImpl implements AccidentService {

    private final AccidentRepository accidentRepository;

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

        Integer totalDeathCnt = calculateTotal(accidentCountPerMonthList, AccidentCountPerMonthDto::getDeathCnt);
        Integer totalDisappearanceCnt = calculateTotal(accidentCountPerMonthList, AccidentCountPerMonthDto::getDisappearanceCnt);
        Integer totalInjuryCnt = calculateTotal(accidentCountPerMonthList, AccidentCountPerMonthDto::getInjuryCnt);

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

    private Integer calculateTotal(List<AccidentCountPerMonthDto> list, ToIntFunction<AccidentCountPerMonthDto> mapper) {
        return list.stream().mapToInt(mapper).sum();
    }
}
