package kr.ac.kumoh.illdang100.tovalley.domain.accident;

import java.util.List;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash("RegionAccidentStatistics")
public class RegionAccidentStatistics {

    @Id
    private String id;

    @Indexed
    private String province; // 행정구역을 Redis의 키로 사용

    private List<AccidentCountPerMonth> accidentCountPerMonth;

    private int totalDeathCnt;
    private int totalDisappearanceCnt;
    private int totalInjuryCnt;

    public void incrementTotalDeathCnt(int cnt) {
        this.totalDeathCnt += cnt;
    }

    public void incrementTotalDisappearanceCnt(int cnt) {
        this.totalDisappearanceCnt += cnt;
    }

    public void incrementTotalInjuryCnt(int cnt) {
        this.totalInjuryCnt += cnt;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class AccidentCountPerMonth {

        private Integer month;
        private Integer deathCnt;
        private Integer disappearanceCnt;
        private Integer injuryCnt;

        public void changeMonth(int month) {
            this.month = month;
        }

        public void incrementDeathCnt(int cnt) {
            this.deathCnt += cnt;
        }

        public void incrementDisappearanceCnt(int cnt) {
            this.disappearanceCnt += cnt;
        }

        public void incrementInjuryCnt(int cnt) {
            this.injuryCnt += cnt;
        }

        public AccidentCountPerMonth(AccidentCountPerMonth other) {
            this.month = other.month;
            this.deathCnt = other.deathCnt;
            this.disappearanceCnt = other.disappearanceCnt;
            this.injuryCnt = other.injuryCnt;
        }
    }
}
