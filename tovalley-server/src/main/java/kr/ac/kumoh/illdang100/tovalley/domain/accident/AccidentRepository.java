package kr.ac.kumoh.illdang100.tovalley.domain.accident;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccidentRepository extends JpaRepository<Accident, Long>, AccidentRepositoryCustom {

    @Query("SELECT a FROM Accident a WHERE YEAR(a.accidentDate) = :year")
    List<Accident> findByYear(@Param("year") int year);

    @Query("select a from Accident a join a.waterPlace wp where wp.province like :province% and YEAR(a.accidentDate) = :year")
    List<Accident> findByProvinceStartingWithProvinceAndYear(@Param("province") String province, @Param("year") int year);

    List<Accident> findByWaterPlaceIdAndAccidentDateAfter(Long waterPlaceId, LocalDate date);

    List<Accident> findByWaterPlaceId(Long waterPlaceId);

    Optional<Accident> findByIdAndWaterPlaceId(Long accidentId, Long waterPlaceId);
}
