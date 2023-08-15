package kr.ac.kumoh.illdang100.tovalley.domain.water_place;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RescueSupplyRepository extends JpaRepository<RescueSupply, Long> {

    Optional<RescueSupply> findByWaterPlace_Id(Long waterPlaceId);
}
