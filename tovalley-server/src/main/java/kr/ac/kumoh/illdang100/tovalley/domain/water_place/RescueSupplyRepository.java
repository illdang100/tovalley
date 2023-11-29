package kr.ac.kumoh.illdang100.tovalley.domain.water_place;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RescueSupplyRepository extends JpaRepository<RescueSupply, Long> {

    Optional<RescueSupply> findByWaterPlace_Id(Long waterPlaceId);

    List<RescueSupply> findByWaterPlace_IdIn(Collection<Long> waterPlaceIds);
}
