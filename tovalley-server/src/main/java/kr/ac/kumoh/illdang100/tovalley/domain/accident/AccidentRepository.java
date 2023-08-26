package kr.ac.kumoh.illdang100.tovalley.domain.accident;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AccidentRepository extends JpaRepository<Accident, Long>, AccidentRepositoryCustom {

    @Query("select a from Accident a join a.waterPlace wp where wp.province like :province%")
    List<Accident> findByProvinceStartingWithProvince(@Param("province") String province);

    List<Accident> findByWaterPlaceIdAndAccidentDateAfter(Long waterPlaceId, LocalDate date);

    List<Accident> findByWaterPlaceId(Long waterPlaceId);
}
