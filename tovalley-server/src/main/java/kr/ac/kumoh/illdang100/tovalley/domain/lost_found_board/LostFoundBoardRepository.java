package kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LostFoundBoardRepository extends JpaRepository<LostFoundBoard, Long>, LostFoundBoardRepositoryCustom {

    @Query("select lfb from LostFoundBoard lfb JOIN FETCH lfb.member m JOIN FETCH lfb.waterPlace wp where lfb.id = :lostFoundBoardId")
    Optional<LostFoundBoard> findByIdWithMemberAndWaterPlace(@Param("lostFoundBoardId")long lostFoundBoardId);
}
