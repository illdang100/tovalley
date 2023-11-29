package kr.ac.kumoh.illdang100.tovalley.dto.rescue_supply;

import kr.ac.kumoh.illdang100.tovalley.domain.water_place.RescueSupply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class RescueSupplyRespDto {

    @AllArgsConstructor
    @Builder
    @Data
    public static class RescueSupplyByWaterPlaceRespDto {

        private Integer lifeBoatNum; // 인명구조함(null)
        private Integer portableStandNum; // 이동식거치대(null)
        private Integer lifeJacketNum; // 구명조끼(null)
        private Integer lifeRingNum; // 구명환(null)
        private Integer rescueRopeNum; // 구명로프(null)
        private Integer rescueRodNum; // 구조봉(null)

        public static RescueSupplyByWaterPlaceRespDto createRescueSupplyRespDto(RescueSupply findRescueSupply) {
            return RescueSupplyByWaterPlaceRespDto.builder()
                    .lifeBoatNum(findRescueSupply.getLifeBoatNum())
                    .portableStandNum(findRescueSupply.getPortableStandNum())
                    .lifeJacketNum(findRescueSupply.getLifeJacketNum())
                    .lifeRingNum(findRescueSupply.getLifeRingNum())
                    .rescueRopeNum(findRescueSupply.getRescueRopeNum())
                    .rescueRodNum(findRescueSupply.getRescueRodNum())
                    .build();
        }
    }
}
