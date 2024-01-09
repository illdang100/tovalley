package kr.ac.kumoh.illdang100.tovalley.domain.accident;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RegionAccidentStatisticsRedisRepository extends CrudRepository<RegionAccidentStatistics, String> {

    Optional<RegionAccidentStatistics> findByProvince(String province);
}
